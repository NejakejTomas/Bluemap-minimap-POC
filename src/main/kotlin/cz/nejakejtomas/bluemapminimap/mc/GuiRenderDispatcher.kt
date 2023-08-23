package cz.nejakejtomas.bluemapminimap.mc

import cz.nejakejtomas.bluemapminimap.LoopDispatcher
import me.x150.renderer.event.RenderEvents

class GuiRenderDispatcher : LoopDispatcher() {
    init {
        RenderEvents.HUD.register { _ ->
            run()
        }
    }
}