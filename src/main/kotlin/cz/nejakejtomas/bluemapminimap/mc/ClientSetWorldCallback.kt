package cz.nejakejtomas.bluemapminimap.mc

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory

fun interface ClientSetWorldCallback {
    fun onSwitch(worldName: String?)

    companion object {
        val EVENT: Event<ClientSetWorldCallback> = EventFactory.createArrayBacked(ClientSetWorldCallback::class.java) { listeners: Array<ClientSetWorldCallback> ->
            ClientSetWorldCallback { worldName ->
                for (listener in listeners) {
                    listener.onSwitch(worldName)
                }
            }
        }
    }
}