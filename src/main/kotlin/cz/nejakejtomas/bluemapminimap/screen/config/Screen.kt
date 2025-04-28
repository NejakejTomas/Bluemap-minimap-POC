package cz.nejakejtomas.bluemapminimap.screen.config

import cz.nejakejtomas.bluemapminimap.model.ServerId
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable
sealed interface Screen {
    @Serializable
    data object General : Screen

    @Serializable
    data object Servers : Screen

    @Serializable
    data class Server(val serverId: ServerId) : Screen
}

val typeMap = mapOf(
    typeOf<ServerId>() to ServerId.ParameterType,
)