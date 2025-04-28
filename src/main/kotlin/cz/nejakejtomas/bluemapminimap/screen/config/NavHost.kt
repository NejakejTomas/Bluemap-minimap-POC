package cz.nejakejtomas.bluemapminimap.screen.config

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import cz.nejakejtomas.bluemapminimap.screen.config.general.MinimapSettingsScreen
import cz.nejakejtomas.bluemapminimap.screen.config.server.ServerScreen
import cz.nejakejtomas.bluemapminimap.screen.config.servers.ServersScreen

@Composable
fun NavHost(
    navController: NavHostController,
    startDestination: Screen,
    modifier: Modifier = Modifier,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
    ) {
        composable<Screen.General> {
            MinimapSettingsScreen(navController)
        }

        composable<Screen.Servers> {
            ServersScreen(navController)
        }

        composable<Screen.Server>(typeMap) {
            val route = it.toRoute<Screen.Server>()
            ServerScreen(route.serverId, navController)
        }
    }
}