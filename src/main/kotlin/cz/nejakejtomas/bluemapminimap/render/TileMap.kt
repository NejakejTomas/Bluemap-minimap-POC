package cz.nejakejtomas.bluemapminimap.render

import com.mojang.blaze3d.platform.NativeImage
import com.mojang.blaze3d.systems.RenderSystem
import cz.nejakejtomas.bluemapminimap.client.MapClient
import kotlinx.coroutines.*
import me.x150.renderer.util.RendererUtils
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.texture.DynamicTexture
import net.minecraft.resources.ResourceLocation
import org.lwjgl.BufferUtils
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

@Suppress("KotlinConstantConditions")
class TileMap(
    private val minecraft: Minecraft,
    private val mapClient: MapClient,
    private val renderDispatcher: CoroutineDispatcher,
    private val tickDispatcher: CoroutineDispatcher,
    private val coroutineScope: CoroutineScope
) {
    private var _tileWidth: Int? = null
    private var _tileHeight: Int? = null
    val tileWidth: Int?
        get() {
            RenderSystem.assertOnRenderThread()
            return _tileWidth
        }
    val tileHeight: Int?
        get() {
            RenderSystem.assertOnRenderThread()
            return _tileHeight
        }

    private var tileCenterX = 0
    private var tileCenterZ = 0

    private var tiles: Array<Array<ResourceLocation?>>

    operator fun get(x: Int, y: Int): ResourceLocation? {
        return tiles[x + 1][y + 1]
    }

    init {
        tiles = Array(UNSAFE_TILE_COUNT_X) { Array(UNSAFE_TILE_COUNT_Z) { null } }

        coroutineScope.launch(Dispatchers.IO) {
            val width = mapClient.tileWidth() ?: return@launch
            val height = mapClient.tileHeight() ?: return@launch

            withContext(renderDispatcher) {
                _tileWidth = width
                _tileHeight = height

                centerAtInternal(tileCenterX, tileCenterZ)
            }
        }
    }

    private suspend fun placeTile(tile: BufferedImage, tileX: Int, tileZ: Int) {
        val (width, height) = withContext(renderDispatcher) {
            ((_tileWidth ?: return@withContext null) to (_tileHeight ?: return@withContext null))
        } ?: return

        val location = RendererUtils.randomIdentifier()

        val texture = withContext(Dispatchers.IO) {
            val byteStream = ByteArrayOutputStream()
            ImageIO.write(tile.getSubimage(0, 0, width, height), "png", byteStream)
            val bytes = byteStream.toByteArray()
            val data = BufferUtils.createByteBuffer(bytes.size).put(bytes)
            data.flip()
            return@withContext DynamicTexture(NativeImage.read(data))
        }

        withContext(tickDispatcher) {
            minecraft.textureManager.register(location, texture)
        }

        withContext(renderDispatcher) {
            val relativeX = tileX - tileCenterX
            val relativeZ = tileZ - tileCenterZ

            if (
                relativeX < 0 ||
                relativeX >= UNSAFE_TILE_COUNT_X ||
                relativeZ < 0 ||
                relativeZ >= UNSAFE_TILE_COUNT_Z
            ) {
                // Fail -> release texture
                withContext(tickDispatcher) {
                    minecraft.textureManager.release(location)
                }
                return@withContext
            }

            tiles[relativeX][relativeZ] = location
        }
    }

    private fun getOldTile(shiftX: Int, shiftZ: Int, x: Int, z: Int): ResourceLocation? {
        RenderSystem.assertOnRenderThread()

        val oldX = x + shiftX
        val oldZ = z + shiftZ

        if (oldX < 0 || oldX >= UNSAFE_TILE_COUNT_X || oldZ < 0 || oldZ >= UNSAFE_TILE_COUNT_Z) {
            // Out of old window - new will be requested
            return null
        }

        val tile = tiles[oldX][oldZ]
        tiles[oldX][oldZ] = null

        return tile
    }

    val tileOffsetX
        get() = if (TILE_COUNT_X % 2 == 0) tileWidth?.div(2) else 0

    val tileOffsetZ
        get() = if (TILE_COUNT_Z % 2 == 0) tileHeight?.div(2) else 0

    private fun centerAtInternal(tileX: Int, tileZ: Int) {
        RenderSystem.assertOnRenderThread()

        val shiftX = tileX - tileCenterX
        val shiftZ = tileZ - tileCenterZ

        val newTiles = Array(UNSAFE_TILE_COUNT_X) { x ->
            Array(UNSAFE_TILE_COUNT_Z) { z ->
                getOldTile(shiftX, shiftZ, x, z)
            }
        }

        // Free old tiles
        tiles.forEach {
            it.forEach { pair ->
                pair?.let { p ->
                    coroutineScope.launch(tickDispatcher) {
                        minecraft.textureManager.release(p)
                    }
                }
            }
        }

        tiles = newTiles

        tileCenterX = tileX
        tileCenterZ = tileZ

        for (x in 0 until UNSAFE_TILE_COUNT_X) {
            for (z in 0 until UNSAFE_TILE_COUNT_Z) {
                if (tiles[x][z] != null) continue
                val realX = tileCenterX + x
                val realZ = tileCenterZ + z

                coroutineScope.launch(Dispatchers.IO) {
                    val tile = mapClient.tileAt(realX, realZ) ?: return@launch
                    placeTile(tile, realX, realZ)
                }
            }
        }
    }

    fun centerAt(x: Int, z: Int) {
        RenderSystem.assertOnRenderThread()

        val width = _tileWidth ?: return
        val height = _tileHeight ?: return

        val correctedX = x - (((UNSAFE_TILE_COUNT_X - 1) / 2.0) * width).toInt()
        val correctedZ = z - (((UNSAFE_TILE_COUNT_Z - 1) / 2.0) * height).toInt()

        val newTileX = Math.floorDiv(correctedX, width)
        val newTileZ = Math.floorDiv(correctedZ, height)

        // We did not move enough
        if (tileCenterX == newTileX && tileCenterZ == newTileZ) return

        // We need to recenter the tilemap
        centerAtInternal(newTileX, newTileZ)
    }

    companion object {
        // TODO: Not static?
        const val TILE_COUNT_X: Int = 2
        const val TILE_COUNT_Z: Int = 2

        init {
            // For my sanity
            assert(TILE_COUNT_X == TILE_COUNT_Z)
        }

        private const val UNSAFE_TILE_COUNT_X: Int = TILE_COUNT_X + 2
        private const val UNSAFE_TILE_COUNT_Z: Int = TILE_COUNT_Z + 2

    }
}