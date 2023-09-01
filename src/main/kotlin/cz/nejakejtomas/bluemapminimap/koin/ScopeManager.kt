package cz.nejakejtomas.bluemapminimap.koin

import cz.nejakejtomas.bluemapminimap.koin.ScopeManager.ScopeChangeCallback
import cz.nejakejtomas.bluemapminimap.mc.ClientSetServerCallback
import cz.nejakejtomas.bluemapminimap.mc.ClientSetWorldCallback
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import org.koin.core.component.KoinComponent
import org.koin.core.scope.Scope

class ScopeManager : KoinComponent {
    private var worldScope: Scope? = null
    private var serverScope: Scope? = null

    val newWorldScope: Event<ScopeChangeCallback> = EventFactory.createArrayBacked(ScopeChangeCallback::class.java) { listeners: Array<ScopeChangeCallback> ->
        ScopeChangeCallback { serverData ->
            for (listener in listeners) {
                listener.newScope(serverData)
            }
        }
    }

    val newServerScope: Event<ScopeChangeCallback> = EventFactory.createArrayBacked(ScopeChangeCallback::class.java) { listeners: Array<ScopeChangeCallback> ->
        ScopeChangeCallback { serverData ->
            for (listener in listeners) {
                listener.newScope(serverData)
            }
        }
    }
    fun configurationChange() {
        val currentWorldName = worldScope?.id
        val currentServerName = serverScope?.id

        if (currentWorldName != null) onWorldChange(null)
        if (currentServerName != null) onServerChange(null)

        if (currentServerName != null) onServerChange(currentServerName)
        if (currentWorldName != null) onWorldChange(currentWorldName)
    }

    fun interface ScopeChangeCallback {
        fun newScope(scope: Scope?)
    }

    private fun onServerChange(serverIp: String?) {
        serverScope?.close()

        val newScope = if (serverIp == null) {
            // Singleplayer or leaving world
            null
        } else {
            // Multiplayer
            getKoin().createScope(serverIp, LifecycleQualifier.Server)
        }

        if (newScope != null) worldScope?.linkTo(newScope)
        serverScope = newScope

        newServerScope.invoker().newScope(newScope)
    }

    private fun onWorldChange(worldName: String?) {
        worldScope?.close()
        val server = serverScope

        val newScope = if (worldName == null || server == null) {
            null
        } else {
            getKoin().createScope(worldName, LifecycleQualifier.World)
        }

        server?.let {
            newScope?.linkTo(it)
        }
        worldScope = newScope
        newWorldScope.invoker().newScope(newScope)
    }

    init {
        ClientSetServerCallback.EVENT.register { serverIp ->
            onServerChange(serverIp)
        }


        ClientSetWorldCallback.EVENT.register { worldName ->
            onWorldChange(worldName)
        }
    }
}