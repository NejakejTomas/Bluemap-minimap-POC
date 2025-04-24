package cz.nejakejtomas.bluemapminimap.client.server

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ServerSettings(
    @SerialName("maps") val maps: List<String>,
    @SerialName("mapDataRoot") val dataRoot: String,
    @SerialName("liveDataRoot") val liveDataRoot: String,
)