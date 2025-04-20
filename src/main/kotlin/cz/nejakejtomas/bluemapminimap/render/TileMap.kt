package cz.nejakejtomas.bluemapminimap.render

import net.minecraft.client.gui.GuiGraphics

interface TileMap {
    fun render(guiGraphics: GuiGraphics, positionX: Double, positionZ: Double, rotation: Float?)
}