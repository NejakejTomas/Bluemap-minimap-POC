package cz.nejakejtomas.bluemapminimap

import cz.nejakejtomas.bluemapminimap.koin.Koin
import cz.nejakejtomas.bluemapminimap.render.GuiRenderable
import me.x150.renderer.event.RenderEvents
import org.koin.core.component.KoinComponent

@Suppress("unused")
fun init() {
    Mod.init()
}

object Mod : KoinComponent {
    fun init() {
        Koin.start()

        val renderables = getKoin().getAll<GuiRenderable>()

        RenderEvents.HUD.register { graphics ->
            renderables.forEach {
                it.render(graphics)
            }
        }
    }

}