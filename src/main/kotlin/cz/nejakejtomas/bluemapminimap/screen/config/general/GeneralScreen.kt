package cz.nejakejtomas.bluemapminimap.screen.config.general

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import cz.nejakejtomas.bluemapminimap.screen.config.TopAppBar
import cz.nejakejtomas.bluemapminimap.screen.config.TopScreen
import cz.nejakejtomas.minimap.resources.Res
import cz.nejakejtomas.minimap.resources.config_general_screen_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneralScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: GeneralViewModel = koinViewModel()
) {
    TopScreen(navController, modifier) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    navController,
                    title = {
                        Text(stringResource(Res.string.config_general_screen_title))
                    }
                )
            }
        ) { innerPadding ->
            Text("TMP General", Modifier.padding(innerPadding))
        }
    }

}