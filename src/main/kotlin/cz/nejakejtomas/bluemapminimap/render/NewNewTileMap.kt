package cz.nejakejtomas.bluemapminimap.render

import com.mojang.blaze3d.platform.NativeImage
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.PoseStack
import cz.nejakejtomas.bluemapminimap.client.MapClient
import cz.nejakejtomas.bluemapminimap.common.Size
import cz.nejakejtomas.bluemapminimap.config.DebugConfig
import kotlinx.coroutines.*
import me.x150.renderer.render.Renderer2d
import me.x150.renderer.util.RendererUtils
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.texture.DynamicTexture
import net.minecraft.resources.ResourceLocation
import org.lwjgl.BufferUtils
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO
import kotlin.math.floor

class NewNewTileMap(
    private val minecraft: Minecraft,
    private val targetBlockSize: Size<Int>,
    private val doRotate: Boolean,
    private val coroutineScope: CoroutineScope,
    private val renderDispatcher: CoroutineDispatcher,
    private val tickDispatcher: CoroutineDispatcher,
    private val mapClient: MapClient,
    private val debugConfig: DebugConfig,
) : TileMap {

    data class Tiles(
        val tilesSize: Size<Int>,
        val tileSize: Size<Int>,
        @Suppress("ArrayInDataClass") val data: Array<Array<ResourceLocation?>>
    )

    private var tiles: Tiles? = null

    init {
        coroutineScope.launch(Dispatchers.IO) {
            val size = mapClient.tileSize() ?: return@launch

            val tilesSize = calculateTilesSize(size)
            val newTiles = Array(tilesSize.width) { Array<ResourceLocation?>(tilesSize.height) { null } }

            withContext(renderDispatcher) {
                Tiles(tilesSize, size, newTiles).let {
                    tiles = it
                    centerAtInternal(it, currentX, currentZ)
                }
            }
        }
    }

    // Center tile
    private var currentX = 0
    private var currentZ = 0

    private fun centerAt(x: Int, z: Int) {
        val tiles = tiles ?: return

        val newTileX = Math.floorDiv(x, tiles.tileSize.width)
        val newTileZ = Math.floorDiv(z, tiles.tileSize.height)

        // We did not move enough
        if (currentX == newTileX && currentZ == newTileZ) return

        centerAtInternal(tiles, newTileX, newTileZ)
    }

    private fun getOldTile(tiles: Tiles, shiftX: Int, shiftZ: Int, x: Int, z: Int): ResourceLocation? {
        RenderSystem.assertOnRenderThread()

        val oldX = x + shiftX
        val oldZ = z + shiftZ

        if (oldX < 0 || oldX >= tiles.tilesSize.width || oldZ < 0 || oldZ >= tiles.tilesSize.height) {
            // Out of old window - new will be requested
            return null
        }

        val tile = tiles.data[oldX][oldZ]
        tiles.data[oldX][oldZ] = null

        return tile
    }

    private suspend fun placeTile(tile: BufferedImage, realTileX: Int, realTileZ: Int) {
        val tiles = tiles ?: return

        val location = RendererUtils.randomIdentifier()

        val texture = withContext(Dispatchers.IO) {
            val byteStream = ByteArrayOutputStream()
            ImageIO.write(tile.getSubimage(0, 0, tiles.tileSize.width, tiles.tileSize.height), "png", byteStream)
            val bytes = byteStream.toByteArray()
            val data = BufferUtils.createByteBuffer(bytes.size).put(bytes)
            data.flip()
            return@withContext DynamicTexture(NativeImage.read(data))
        }

        withContext(tickDispatcher) {
            minecraft.textureManager.register(location, texture)
        }

        withContext(renderDispatcher) {
            val relativeX = realTileX - currentX
            val relativeZ = realTileZ - currentZ

            if (
                relativeX < 0 ||
                relativeX >= tiles.tilesSize.width ||
                relativeZ < 0 ||
                relativeZ >= tiles.tilesSize.height
            ) {
                // Fail -> release texture
                withContext(tickDispatcher) {
                    minecraft.textureManager.release(location)
                }
                return@withContext
            }

            tiles.data[relativeX][relativeZ] = location
        }
    }

    private fun floatMod(x: Double, y: Double): Double {
        // x mod y behaving the same way as Math.floorMod but with doubles
        return x - floor(x / y) * y
    }

    override fun render(pose: PoseStack, positionX: Double, positionZ: Double, rotation: Float?) {
        RenderSystem.assertOnRenderThread()
        centerAt(floor(positionX).toInt(), floor(positionZ).toInt())

        val tiles = tiles ?: return

        val centerX =
            ((tiles.tileSize.width * (tiles.tilesSize.width - 2 * TILE_DOWNLOAD_BORDER_BUFFER_SIZE) - targetBlockSize.width) / 2).toDouble()
        val centerY =
            ((tiles.tileSize.height * (tiles.tilesSize.height - 2 * TILE_DOWNLOAD_BORDER_BUFFER_SIZE) - targetBlockSize.height) / 2).toDouble()


        val playerTileOffsetX = (floatMod(positionX, tiles.tileSize.width.toDouble()))
        val playerTileOffsetZ = (floatMod(positionZ, tiles.tileSize.height.toDouble()))

        pose.translate(
            -centerX,
            -centerY,
            0.0
        )

        pose.translate(-playerTileOffsetX, -playerTileOffsetZ, 0.0)

//        rotation?.also {
//            pose.rotateAround(
//                Quaternionf().apply {
//                    rotateZ(-Math.toRadians((it.toDouble() + 180)).toFloat())
//                },
//                (tiles.tileSize.width * (tiles.tilesSize.width / 2 - TILE_DOWNLOAD_BORDER_BUFFER_SIZE) + playerTileOffsetX).toFloat(),
//                (tiles.tileSize.height * (tiles.tilesSize.height / 2 - TILE_DOWNLOAD_BORDER_BUFFER_SIZE) + playerTileOffsetZ).toFloat(),
//                0f,
//            )
//        }

        for (x in TILE_DOWNLOAD_BORDER_BUFFER_SIZE until tiles.tilesSize.width - TILE_DOWNLOAD_BORDER_BUFFER_SIZE) {
            for (z in TILE_DOWNLOAD_BORDER_BUFFER_SIZE until tiles.tilesSize.height - TILE_DOWNLOAD_BORDER_BUFFER_SIZE) {
                val tile = tiles.data[x][z]

                val offsetX = x - TILE_DOWNLOAD_BORDER_BUFFER_SIZE
                val offsetZ = z - TILE_DOWNLOAD_BORDER_BUFFER_SIZE

                if (tile != null) {
                    Renderer2d.renderTexture(
                        pose,
                        tile,
                        (offsetX * tiles.tileSize.width).toDouble(),
                        (offsetZ * tiles.tileSize.height).toDouble(),
                        tiles.tileSize.width.toDouble(),
                        tiles.tileSize.height.toDouble(),
                    )
                }
                if (debugConfig.config.value.debugRender) {
                    Renderer2d.renderQuad(
                        pose,
                        if (offsetX % 2 == offsetZ % 2) Color(255, 0, 0, 50) else Color(0, 0, 255, 50),
                        (offsetX * tiles.tileSize.width).toDouble(),
                        (offsetZ * tiles.tileSize.height).toDouble(),
                        ((offsetX + 1) * tiles.tileSize.width).toDouble(),
                        ((offsetZ + 1) * tiles.tileSize.height).toDouble(),
                    )
                }
            }
        }
    }

    private fun centerAtInternal(tiles: Tiles, tileX: Int, tileZ: Int) {
        RenderSystem.assertOnRenderThread()
        val shiftX = tileX - currentX
        val shiftZ = tileZ - currentZ

        val newTiles = Array(tiles.tilesSize.width) { x ->
            Array(tiles.tilesSize.height) { z ->
                getOldTile(tiles, shiftX, shiftZ, x, z)
            }
        }

        // Free old tiles
        tiles.data.forEach {
            it.forEach { pair ->
                pair?.let { p ->
                    coroutineScope.launch(tickDispatcher) {
                        minecraft.textureManager.release(p)
                    }
                }
            }
        }

        this.tiles = tiles.copy(data = newTiles)

        currentX = tileX
        currentZ = tileZ

        for (x in 0 until tiles.tilesSize.width) {
            for (z in 0 until tiles.tilesSize.height) {
                if (tiles.data[x][z] != null) continue

                coroutineScope.launch(Dispatchers.IO) {
                    // +1 to offset the render border
                    val tile = mapClient.tileAt(
                        (tileX + x - tiles.tilesSize.width / 2),
                        (tileZ + z - tiles.tilesSize.height / 2),
                    ) ?: return@launch
                    placeTile(tile, tileX + x, tileZ + z)
                }
            }
        }
    }

    private fun calculateTilesSize(tileSize: Size<Int>): Size<Int> {
        // Round up, add two to have some buffer
        val tilesNeededWidth = (targetBlockSize.width + (tileSize.width - 1)) / tileSize.width
        val tilesNeededHeight = (targetBlockSize.height + (tileSize.height - 1)) / tileSize.height
        // 1 + 1 tile from each side, so we have time to download another tiles and 1 tile from each side, and we can
        // move "camera"
        val width = tilesNeededWidth + 2 * TILE_DOWNLOAD_BORDER_BUFFER_SIZE + 2 * TILE_RENDER_BORDER_BUFFER_SIZE
        val height = tilesNeededHeight + 2 * TILE_DOWNLOAD_BORDER_BUFFER_SIZE + 2 * TILE_RENDER_BORDER_BUFFER_SIZE

        return Size(width, height)
    }

    companion object {
        private const val TILE_DOWNLOAD_BORDER_BUFFER_SIZE = 1
        private const val TILE_RENDER_BORDER_BUFFER_SIZE = 1
    }
}