package cz.nejakejtomas.bluemapminimap.screen.config.server

data class ServerUiState(
    val serverUrl: String,
    // TODO: Name?
    val savedMapUrl: String = "",
    val mapUrlHint: String = "",
)