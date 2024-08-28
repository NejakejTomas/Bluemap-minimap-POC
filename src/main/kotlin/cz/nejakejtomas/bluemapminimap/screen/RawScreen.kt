package cz.nejakejtomas.bluemapminimap.screen

import io.github.cottonmc.cotton.gui.GuiDescription
import io.github.cottonmc.cotton.gui.client.CottonClientScreen
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription
import io.github.cottonmc.cotton.gui.widget.WButton
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WLabel
import io.github.cottonmc.cotton.gui.widget.WSprite
import io.github.cottonmc.cotton.gui.widget.data.Insets
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation


class RawScreen(description: GuiDescription, val parent: Screen) : CottonClientScreen(description) {

    override fun onClose() {
        super.onClose()
        minecraft!!.setScreen(parent)
    }
}
class ExampleGui : LightweightGuiDescription() {
    init {
        val root = WGridPanel()
        setRootPanel(root)
        root.setSize(256, 240)
        root.setInsets(Insets.ROOT_PANEL)

        val icon = WSprite(ResourceLocation.parse("minecraft:textures/item/redstone.png"))
        root.add(icon, 0, 2, 1, 1)

        val button = WButton(Component.translatable("gui.examplemod.examplebutton"))
        var i = 0
        button.setOnClick {
            val label = WLabel(Component.literal("Clicked ${++i} times"), 0xFFFFFF)
            root.add(label, 0, 4 + i, 2, 1)
//            root.validate(this)
        }
        root.add(button, 0, 3, 4, 1)

        val label = WLabel(Component.literal("Test"), 0xFFFFFF)
        root.add(label, 0, 4, 2, 1)

        root.validate(this)
    }
}