package cz.nejakejtomas.bluemapminimap.screen.config

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import cz.nejakejtomas.composescreen.LocalScreen
import cz.nejakejtomas.minimap.resources.Res
import cz.nejakejtomas.minimap.resources.config_title_back
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    navController: NavController,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {},
) {
    TopAppBar(
        title = title,
        modifier = modifier,
        navigationIcon = icon@{
            val screen = LocalScreen.current
            val canGoBack = navController.previousBackStackEntry != null || screen != null

            if (!canGoBack) return@icon

            IconButton(onClick = { if (!navController.navigateUp()) screen?.onClose() }) {
                Icon(Icons.AutoMirrored.Default.ArrowBack, stringResource(Res.string.config_title_back))
            }
        },
        actions = actions,
    )
}