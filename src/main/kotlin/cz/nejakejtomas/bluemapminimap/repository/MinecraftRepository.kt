package cz.nejakejtomas.bluemapminimap.repository

import cz.nejakejtomas.bluemapminimap.mc.ClientSetServerCallback
import cz.nejakejtomas.bluemapminimap.mc.ClientSetWorldCallback
import cz.nejakejtomas.bluemapminimap.mc.currentWorldName
import cz.nejakejtomas.bluemapminimap.model.ServerId
import cz.nejakejtomas.bluemapminimap.model.WorldId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import net.minecraft.client.Minecraft

class MinecraftRepository(
    minecraft: Minecraft,
) {
    private val _serverId = MutableStateFlow(minecraft.currentServer?.let { ServerId(it.ip) })
    val server = _serverId.asStateFlow()
    private val _worldId = MutableStateFlow(minecraft.currentWorldName?.let { WorldId(it) })
    val world = _worldId.asStateFlow()

    init {
        ClientSetServerCallback.Companion.EVENT.register { serverIp ->
            _serverId.value = serverIp?.let { ServerId(it) }
        }

        ClientSetWorldCallback.Companion.EVENT.register { worldName ->
            _worldId.value = worldName?.let { WorldId(it) }
        }
    }
}