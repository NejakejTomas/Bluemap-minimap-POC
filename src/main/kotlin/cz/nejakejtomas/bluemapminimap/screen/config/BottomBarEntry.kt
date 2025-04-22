package cz.nejakejtomas.bluemapminimap.screen.config

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import cz.nejakejtomas.minimap.resources.Res
import cz.nejakejtomas.minimap.resources.config_general_screen_navigation_title
import cz.nejakejtomas.minimap.resources.config_general_screen_title
import cz.nejakejtomas.minimap.resources.config_servers_screen_navigation_title
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

enum class BottomBarEntry(
    val route: Screen,
    private val string: StringResource,
    private val iconVector: ImageVector,
) {
    General(
        Screen.General,
        Res.string.config_general_screen_navigation_title,
        Icons.Default.Home,
    ),
    Servers(
        Screen.Servers,
        Res.string.config_servers_screen_navigation_title,
        Icons.Default.Info,
    );

    val label: @Composable () -> Unit = {
        Text(stringResource(string))
    }

    val icon: @Composable () -> Unit = {
        Icon(
            iconVector,
            contentDescription = stringResource(Res.string.config_general_screen_title)
        )
    }
}