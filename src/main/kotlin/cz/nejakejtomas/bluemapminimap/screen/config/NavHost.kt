package cz.nejakejtomas.bluemapminimap.screen.config

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import cz.nejakejtomas.bluemapminimap.screen.config.general.GeneralScreen
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
            GeneralScreen(navController)
        }

        composable<Screen.Servers> {
            ServersScreen(navController)
        }
    }
}