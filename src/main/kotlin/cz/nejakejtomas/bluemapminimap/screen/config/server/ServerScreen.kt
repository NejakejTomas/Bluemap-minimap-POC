package cz.nejakejtomas.bluemapminimap.screen.config.server

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import cz.nejakejtomas.bluemapminimap.model.ServerId
import cz.nejakejtomas.bluemapminimap.screen.config.TopAppBar
import cz.nejakejtomas.bluemapminimap.screen.config.TopScreen
import cz.nejakejtomas.minimap.resources.Res
import cz.nejakejtomas.minimap.resources.config_server_screen_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun MinimalDialog(onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Text(
                text = "This is a minimal dialog",
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center),
                textAlign = TextAlign.Center,
            )
        }
    }
}


@Composable
fun ServerScreen(
    serverId: ServerId,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: ServerViewModel = koinViewModel { parametersOf(serverId) }
) {

    TopScreen(navController, modifier) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    navController,
                    title = {
                        Text(stringResource(Res.string.config_server_screen_title))
                    }
                )
            },
        ) { innerPadding ->
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()


        }
    }
}