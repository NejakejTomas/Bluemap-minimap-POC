@file:Suppress("DEPRECATION", "UnstableApiUsage")

package cz.nejakejtomas.bluemapminimap.screen

import cz.nejakejtomas.bluemapminimap.valueOrNull
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import me.shedaniel.clothconfig2.gui.entries.TooltipListEntry
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.events.GuiEventListener
import net.minecraft.client.gui.narration.NarratableEntry
import net.minecraft.network.chat.Component
import java.util.*
import java.util.function.Consumer
import java.util.function.Supplier
import kotlin.jvm.optionals.getOrDefault

class BooleanListEntry(
    fieldName: Component,
    private val original: Deferred<Boolean>,
    resetButtonKey: Component,
    defaultValue: Supplier<Deferred<Boolean>>?,
    saveConsumer: Consumer<Deferred<Boolean>>,
    tooltipSupplier: Supplier<Optional<Array<Component>>>?,
    requiresRestart: Boolean
) : TooltipListEntry<Deferred<Boolean>>(fieldName, tooltipSupplier, requiresRestart) {
    private var bool: Boolean?
    private val buttonWidget: Button
    private val resetButton: Button
    private val defaultValue: Deferred<Boolean>?
    private val widgets: List<AbstractWidget>

    init {
        this.defaultValue = defaultValue?.get()

        synchronized(this) {
            bool = original.valueOrNull()
        }

        buttonWidget = Button.builder(Component.empty()) {
            synchronized(this) {
                bool = bool?.not()
            }
        }.bounds(0, 0, 150, 20).build()

        resetButton = Button.builder(resetButtonKey) {
            synchronized(this) {
                bool = defaultValue?.get()?.valueOrNull()
            }
        }.bounds(0, 0, Minecraft.getInstance().font.width(resetButtonKey) + 6, 20).build()

        saveCallback = saveConsumer
        widgets = listOf(buttonWidget, resetButton)
    }

    override fun isEdited(): Boolean {
        return synchronized(this) {
            super.isEdited() || original.valueOrNull() != bool
        }
    }

    override fun getValue(): Deferred<Boolean> {
        synchronized(this) {
            return bool?.let {
                CompletableDeferred(it)
            } ?: original
        }
    }

    override fun getDefaultValue(): Optional<Deferred<Boolean>> {
        return Optional.ofNullable(defaultValue)
    }

    override fun render(
        graphics: GuiGraphics,
        index: Int,
        y: Int,
        x: Int,
        entryWidth: Int,
        entryHeight: Int,
        mouseX: Int,
        mouseY: Int,
        isHovered: Boolean,
        delta: Float
    ) {
        super.render(graphics, index, y, x, entryWidth, entryHeight, mouseX, mouseY, isHovered, delta)
        val window = Minecraft.getInstance().window
        synchronized(this) {
            if (bool == null) bool = original.valueOrNull()

            val defaultValue = getDefaultValue().getOrDefault(CompletableDeferred(null)).valueOrNull()
            resetButton.active =
                isEditable && getDefaultValue().isPresent && defaultValue != null && bool != null && defaultValue != bool
            resetButton.y = y
            buttonWidget.active = isEditable
            buttonWidget.y = y
            buttonWidget.message = getYesNoText(bool)
        }
        val displayedFieldName = displayedFieldName
        if (Minecraft.getInstance().font.isBidirectional) {
            graphics.drawString(
                Minecraft.getInstance().font,
                displayedFieldName.visualOrderText,
                window.guiScaledWidth - x - Minecraft.getInstance().font.width(displayedFieldName),
                y + 6,
                16777215
            )
            resetButton.x = x
            buttonWidget.x = x + resetButton.width + 2
        } else {
            graphics.drawString(
                Minecraft.getInstance().font,
                displayedFieldName.visualOrderText,
                x,
                y + 6,
                preferredTextColor
            )
            resetButton.x = x + entryWidth - resetButton.width
            buttonWidget.x = x + entryWidth - 150
        }
        buttonWidget.width = 150 - resetButton.width - 2
        resetButton.render(graphics, mouseX, mouseY, delta)
        buttonWidget.render(graphics, mouseX, mouseY, delta)
    }

    fun getYesNoText(bool: Boolean?): Component {
        // TMP
        val stage = (System.currentTimeMillis() / 1000) % 3
        return when (bool) {
            true -> Component.literal("True")
            false -> Component.literal("False")
            null -> Component.literal("").apply {
                siblings.apply {
                    add(Component.literal(".").withStyle { if (stage != 0L) it.withColor(0x808080) else it })
                    add(Component.literal(".").withStyle { if (stage != 1L) it.withColor(0x808080) else it })
                    add(Component.literal(".").withStyle { if (stage != 2L) it.withColor(0x808080) else it })
                }
            }
        }
    }

    override fun children(): List<GuiEventListener?> {
        return widgets
    }

    override fun narratables(): List<NarratableEntry?> {
        return widgets
    }
}