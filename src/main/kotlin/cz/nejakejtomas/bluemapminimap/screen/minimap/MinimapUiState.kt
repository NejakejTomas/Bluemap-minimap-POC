package cz.nejakejtomas.bluemapminimap.screen.minimap

import cz.nejakejtomas.bluemapminimap.model.MapDimensionId
import cz.nejakejtomas.bluemapminimap.model.MapId

data class MinimapUiState(
    val mapId: MapId? = null,
    val mapRoot: String? = null,
    val mapDimensionId: MapDimensionId? = null,
)
