package cz.nejakejtomas.bluemapminimap.screen.minimap

import cz.nejakejtomas.bluemapminimap.common.Size
import cz.nejakejtomas.bluemapminimap.model.MapDimensionId
import cz.nejakejtomas.bluemapminimap.model.MapId

data class MinimapUiState(
    val enabled: Boolean = false,
    val mapId: MapId? = null,
    val mapRoot: String? = null,
    val mapDimensionId: MapDimensionId? = null,
    val doRotate: Boolean = false,
    val debugRender: Boolean = false,
    val targetBlockSize: Size<Int> = Size(750, 750)
)
