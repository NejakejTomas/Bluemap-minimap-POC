package cz.nejakejtomas.bluemapminimap.render

import cz.nejakejtomas.bluemapminimap.koin.ScopeManager
import me.x150.renderer.render.ClipStack
import me.x150.renderer.render.MSAAFramebuffer
import me.x150.renderer.render.Renderer2d
import me.x150.renderer.util.Rectangle
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import org.joml.Quaternionf
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.awt.Color
import kotlin.math.floor
import kotlin.math.sqrt

class Minimap : KoinComponent, GuiRenderable {
    private val minecraft: Minecraft by inject()
    private val scopeManager: ScopeManager by inject()

    private var _tileMap: TileMap? = null

    init {
        scopeManager.newWorldScope.register {
            _tileMap = it?.get()
        }
    }

    private fun floatMod(x: Double, y: Double): Double {
        // x mod y behaving the same way as Math.floorMod but with doubles
        return x - floor(x / y) * y
    }

    override fun render(graphics: GuiGraphics) {
        val posX = minecraft.player!!.x
        val posZ = minecraft.player!!.z
        val rot = minecraft.player!!.yRot

        val tileMap = _tileMap ?: return
        val tileWidth = tileMap.tileWidth ?: return
        val tileHeight = tileMap.tileHeight ?: return

        tileMap.centerAt(floor(posX).toInt(), floor(posZ).toInt())

        val widthX = 75.0
        val widthZ = 75.0
        val oneSizeX = widthX / TileMap.TILE_COUNT_X
        val oneSizeZ = widthZ / TileMap.TILE_COUNT_Z
        var currentX = 0.0
        var currentZ = 0.0

        val playerTileOffsetX = (floatMod(posX + tileMap.tileOffsetX!!, tileWidth.toDouble()) / tileWidth) * oneSizeX
        val playerTileOffsetZ = (floatMod(posZ + tileMap.tileOffsetZ!!, tileHeight.toDouble()) / tileHeight) * oneSizeZ

        MSAAFramebuffer.use(32) {
            graphics.pose().pushPose()
            // Offset map from top and left
            graphics.pose().translate(10.0, 10.0, 0.0)

            graphics.pose().pushPose()
            ClipStack.addWindow(graphics.pose(), Rectangle(0.0, 0.0, widthX, widthZ))

            graphics.pose().rotateAround(Quaternionf().apply {
                rotateZ(-Math.toRadians((rot + 180).toDouble()).toFloat())
            }, (widthX / 2).toFloat(), (widthZ / 2).toFloat(), 0f)

            // Scale it so it fills whole square when rotated
            graphics.pose().translate((-sqrt(2.0) * widthX + widthX) / 2, (-sqrt(2.0) * widthZ + widthZ) / 2, 0.0)
            graphics.pose().scale(
                sqrt(2.0).toFloat(),
                sqrt(2.0).toFloat(),
                0.0f
            )

            // Scale according to number of tiles and translate
            graphics.pose().scale(
                TileMap.TILE_COUNT_X.toFloat() / (TileMap.TILE_COUNT_X - 1),
                TileMap.TILE_COUNT_X.toFloat() / (TileMap.TILE_COUNT_X - 1),
                0.0f
            )
            graphics.pose().translate(-playerTileOffsetX, -playerTileOffsetZ, 0.0)

            for (x in 0 until TileMap.TILE_COUNT_X) {
                for (z in 0 until TileMap.TILE_COUNT_Z) {
                    if (tileMap[x, z] != null) {
                        Renderer2d.renderTexture(graphics.pose(), tileMap[x, z], currentX, currentZ, oneSizeX, oneSizeZ)
                    }

                    currentZ += oneSizeZ
                }
                currentZ = 0.0
                currentX += oneSizeX
            }

            ClipStack.popWindow()
            graphics.pose().popPose()
            // Top
            Renderer2d.renderLine(graphics.pose(), Color.BLACK, 0.0, 0.0, widthX, 0.0)
            // Bottom
            Renderer2d.renderLine(graphics.pose(), Color.BLACK, 0.0, widthZ, widthX, widthZ)
            // Left
            Renderer2d.renderLine(graphics.pose(), Color.BLACK, 0.0, 0.0, 0.0, widthZ)
            // Right
            Renderer2d.renderLine(graphics.pose(), Color.BLACK, widthX, 0.0, widthX, widthZ)

            Renderer2d.renderCircle(graphics.pose(), Color.RED, widthX / 2, widthZ / 2, 1.0, 20)

            graphics.pose().popPose()
        }
    }
}