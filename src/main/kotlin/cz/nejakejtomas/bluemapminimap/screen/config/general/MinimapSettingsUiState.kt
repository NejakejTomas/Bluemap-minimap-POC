package cz.nejakejtomas.bluemapminimap.screen.config.general

data class MinimapSettingsUiState(
    val enabled: Boolean,
    val doRotate: Boolean,
    val debugRender: Boolean,
    val targetSizeBlocks: String,
    val targetSizeBlocksError: Boolean,
)
