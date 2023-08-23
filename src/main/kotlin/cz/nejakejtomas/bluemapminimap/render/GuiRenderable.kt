package cz.nejakejtomas.bluemapminimap.render

import net.minecraft.client.gui.GuiGraphics

interface GuiRenderable {
    fun render(graphics: GuiGraphics)
}