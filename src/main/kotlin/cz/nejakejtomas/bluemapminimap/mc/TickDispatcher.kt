package cz.nejakejtomas.bluemapminimap.mc

import cz.nejakejtomas.bluemapminimap.LoopDispatcher
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents

class TickDispatcher : LoopDispatcher() {
    init {
        ClientTickEvents.START_CLIENT_TICK.register {
            run()
        }
    }
}