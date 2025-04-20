package cz.nejakejtomas.bluemapminimap.screen.minimap

import io.ktor.http.*

data class MinimapUiState(
    val mapUrl: Url? = null,
    val mapName: String? = null,
)
