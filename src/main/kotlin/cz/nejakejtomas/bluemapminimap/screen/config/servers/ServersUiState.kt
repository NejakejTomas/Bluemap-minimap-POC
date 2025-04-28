package cz.nejakejtomas.bluemapminimap.screen.config.servers

import cz.nejakejtomas.bluemapminimap.model.ServerId

data class ServersUiState(
    val currentServer: Server? = null,
    val allServers: List<ServersUiState> = listOf(),
) {
    data class Server(
        val serverId: ServerId,
        val serverUrl: String,
        val savedMapUrl: String,
        val mapUrlHint: String,
        // TODO
//        val name: String,
    )
}