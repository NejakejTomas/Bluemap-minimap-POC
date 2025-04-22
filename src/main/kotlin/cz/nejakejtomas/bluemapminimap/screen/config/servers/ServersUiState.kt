package cz.nejakejtomas.bluemapminimap.screen.config.servers

import cz.nejakejtomas.bluemapminimap.model.ServerId

data class ServersUiState(
    val currentServer: Server? = null,
    val allServers: List<ServersUiState> = listOf(),
) {
    data class Server(
        val id: ServerId,
        val mapUrl: String,
        // TODO
//        val name: String,
    )
}