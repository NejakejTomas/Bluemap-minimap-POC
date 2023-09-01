package cz.nejakejtomas.bluemapminimap.mc

import cz.nejakejtomas.bluemapminimap.LoopDispatcher
import me.x150.renderer.event.RenderEvents
import org.koin.core.qualifier.Qualifier

class GuiRenderDispatcher : LoopDispatcher() {
    init {
        RenderEvents.HUD.register { _ ->
            run()
        }
    }

    companion object : Qualifier {
        override val value = "GuiRenderDispatcher"
    }
}