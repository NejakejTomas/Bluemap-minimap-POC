package cz.nejakejtomas.bluemapminimap

import cz.nejakejtomas.bluemapminimap.client.networkModule
import cz.nejakejtomas.bluemapminimap.render.GuiRenderable
import cz.nejakejtomas.bluemapminimap.screen.config.configModule
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
            modules(module, configModule, networkModule)
        }

        val renderables = getKoin().getAll<GuiRenderable>()

        RenderEvents.HUD.register { graphics ->
            renderables.forEach {
                it.render(graphics)
            }
        }
    }

}