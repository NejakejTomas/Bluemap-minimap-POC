package cz.nejakejtomas.bluemapminimap.mc

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory

fun interface ClientSetServerCallback {
    fun onSwitch(serverIp: String?)

    companion object {
        val EVENT: Event<ClientSetServerCallback> = EventFactory.createArrayBacked(ClientSetServerCallback::class.java) { listeners: Array<ClientSetServerCallback> ->
            ClientSetServerCallback { serverIp ->
                for (listener in listeners) {
                    listener.onSwitch(serverIp)
                }
            }
        }
    }
}