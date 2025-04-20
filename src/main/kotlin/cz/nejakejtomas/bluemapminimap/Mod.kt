package cz.nejakejtomas.bluemapminimap

import cz.nejakejtomas.bluemapminimap.render.GuiRenderable
import me.x150.renderer.event.RenderEvents
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin

@Suppress("unused")
fun init() {
    Mod.init()
}

object Mod : KoinComponent {
    fun init() {
        startKoin {
            modules(module)
        }

        val renderables = getKoin().getAll<GuiRenderable>()

        RenderEvents.HUD.register { graphics ->
            renderables.forEach {
                it.render(graphics)
            }
        }
    }

}