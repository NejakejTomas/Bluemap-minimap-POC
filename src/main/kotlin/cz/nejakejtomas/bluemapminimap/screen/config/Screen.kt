package cz.nejakejtomas.bluemapminimap.screen.config

import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen {
    @Serializable
    data object General : Screen

    @Serializable
    data object Servers : Screen
}