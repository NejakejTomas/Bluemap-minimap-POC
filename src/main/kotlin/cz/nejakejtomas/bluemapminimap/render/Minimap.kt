package cz.nejakejtomas.bluemapminimap.render

import cz.nejakejtomas.bluemapminimap.common.Size
import cz.nejakejtomas.bluemapminimap.config.DebugConfig
import cz.nejakejtomas.bluemapminimap.koin.ScopeManager
import me.x150.renderer.render.Renderer2d
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import org.koin.core.parameter.parametersOf
import java.awt.Color

class Minimap(private val minecraft: Minecraft, scopeManager: ScopeManager, private val debugConfig: DebugConfig) :
    GuiRenderable {
    private val size = Size(750, 750)

    private var _tileMap: TileMap? = null

    init {
        scopeManager.newWorldScope.register {
            _tileMap = it?.get { parametersOf(size, true) }
        }
    }

    override fun render(graphics: GuiGraphics) {
        val tileMap = _tileMap ?: return
        val player = minecraft.player ?: return

        val screenSize = Size(75.0, 75.0)

        val scaleWidth = screenSize.width / size.width
        val scaleHeight = screenSize.height / size.height

        graphics.pose().withPose {
            // Offset map from top and left
            translate(10.0, 10.0, 0.0)

            // Render only in set window
//            withWindow(Rectangle(0.0, 0.0, screenSize.width, screenSize.height)) {
            withPose {
                scale(scaleWidth.toFloat(), scaleHeight.toFloat(), 1f)

                // TMP
                tileMap as NewNewTileMap

                tileMap.render(graphics.pose(), player.x, player.z, player.yRot)
                }

            if (debugConfig.config.value.debugRender) {
                Renderer2d.renderLine(graphics.pose(), Color.green, 0.0, 0.0, screenSize.width, screenSize.height)
                Renderer2d.renderLine(graphics.pose(), Color.green, 0.0, screenSize.height, screenSize.width, 0.0)
            }
//            }
        }


        //val posX = minecraft.player!!.x
        //val posZ = minecraft.player!!.z
        //val rot = minecraft.player!!.yRot

        //val tileMap = _tileMap ?: return
        //val tileWidth = tileMap.tileWidth ?: return
        //val tileHeight = tileMap.tileHeight ?: return

        //tileMap.centerAt(floor(posX).toInt(), floor(posZ).toInt())

        //val widthX = 75.0
        //val widthZ = 75.0
        //val oneSizeX = widthX / OldTileMap.TILE_COUNT_X
        //val oneSizeZ = widthZ / OldTileMap.TILE_COUNT_Z
        //var currentX = 0.0
        //var currentZ = 0.0

        //val playerTileOffsetX = (floatMod(posX + tileMap.tileOffsetX!!, tileWidth.toDouble()) / tileWidth) * oneSizeX
        //val playerTileOffsetZ = (floatMod(posZ + tileMap.tileOffsetZ!!, tileHeight.toDouble()) / tileHeight) * oneSizeZ


//        graphics.pose().rotateAround(Quaternionf().apply {
//            rotateZ(-Math.toRadians((rot + 180).toDouble()).toFloat())
//        }, (widthX / 2).toFloat(), (widthZ / 2).toFloat(), 0f)
//
//        // Scale it so it fills whole square when rotated
//        graphics.pose().translate((-sqrt(2.0) * widthX + widthX) / 2, (-sqrt(2.0) * widthZ + widthZ) / 2, 0.0)
//        graphics.pose().scale(
//            sqrt(2.0).toFloat(),
//            sqrt(2.0).toFloat(),
//            0.0f
//        )
//
//        // Scale according to number of tiles and translate
//        graphics.pose().scale(
//            OldTileMap.TILE_COUNT_X.toFloat() / (OldTileMap.TILE_COUNT_X - 1),
//            OldTileMap.TILE_COUNT_X.toFloat() / (OldTileMap.TILE_COUNT_X - 1),
//            0.0f
//        )
//        graphics.pose().translate(-playerTileOffsetX, -playerTileOffsetZ, 0.0)
//
//        for (x in 0 until OldTileMap.TILE_COUNT_X) {
//            for (z in 0 until OldTileMap.TILE_COUNT_Z) {
//                val tile = tileMap[x, z]
//                if (tile != null) {
//                    Renderer2d.renderTexture(
//                        graphics.pose(),
//                        tile,
//                        currentX,
//                        currentZ,
//                        oneSizeX,
//                        oneSizeZ
//                    )
//                }
//
//                currentZ += oneSizeZ
//            }
//            currentZ = 0.0
//            currentX += oneSizeX
//        }

//        ClipStack.popWindow()
//        graphics.pose().popPose()
//        // Top
//        Renderer2d.renderLine(graphics.pose(), Color.BLACK, 0.0, 0.0, widthX, 0.0)
//        // Bottom
//        Renderer2d.renderLine(graphics.pose(), Color.BLACK, 0.0, widthZ, widthX, widthZ)
//        // Left
//        Renderer2d.renderLine(graphics.pose(), Color.BLACK, 0.0, 0.0, 0.0, widthZ)
//        // Right
//        Renderer2d.renderLine(graphics.pose(), Color.BLACK, widthX, 0.0, widthX, widthZ)
//
//        Renderer2d.renderCircle(graphics.pose(), Color.RED, widthX / 2, widthZ / 2, 1.0, 20)
//
//        graphics.pose().popPose()
    }
}
