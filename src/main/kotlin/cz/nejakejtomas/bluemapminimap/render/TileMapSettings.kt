package cz.nejakejtomas.bluemapminimap.render

import cz.nejakejtomas.bluemapminimap.common.Size

data class TileMapSettings(
    val doRotate: Boolean,
    val targetBlockSize: Size<Int>,
)
