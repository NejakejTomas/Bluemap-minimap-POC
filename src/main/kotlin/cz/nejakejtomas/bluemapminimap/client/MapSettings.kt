package cz.nejakejtomas.bluemapminimap.client

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MapSettings(@SerialName("lowres") val tilesSettings: TilesSettings) {
    @Serializable
    data class TilesSettings(@SerialName("tileSize") private val tileSize: List<Int>) {

        val width
            get() = tileSize[0]
        val height
            get() = tileSize[1]
    }
}
