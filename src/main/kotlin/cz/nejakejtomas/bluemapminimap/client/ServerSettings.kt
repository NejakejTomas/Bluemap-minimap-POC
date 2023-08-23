package cz.nejakejtomas.bluemapminimap.client

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ServerSettings(@SerialName("maps") val maps: List<String>)
