package cz.nejakejtomas.bluemapminimap.screen.config

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopScreen(navController: NavController, modifier: Modifier = Modifier, content: @Composable (() -> Unit) = {}) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationSuiteScaffold(
        modifier = modifier,
        layoutType = NavigationSuiteType.NavigationRail,
        navigationSuiteItems = {
            BottomBarEntry.entries.forEach { entry ->
                item(
                    icon = entry.icon,
                    label = entry.label,
                    selected = currentDestination?.hierarchy?.any { it.hasRoute(entry.route::class) } == true,
                    onClick = {
                        navController.navigate(entry.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        },
        content = content
    )

}