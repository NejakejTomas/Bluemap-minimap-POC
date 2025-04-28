package cz.nejakejtomas.bluemapminimap.screen.config.servers

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import cz.nejakejtomas.bluemapminimap.screen.config.Screen
import cz.nejakejtomas.bluemapminimap.screen.config.TopAppBar
import cz.nejakejtomas.bluemapminimap.screen.config.TopScreen
import cz.nejakejtomas.minimap.resources.Res
import cz.nejakejtomas.minimap.resources.config_servers_screen_allServers
import cz.nejakejtomas.minimap.resources.config_servers_screen_currentServer
import cz.nejakejtomas.minimap.resources.config_servers_screen_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
private fun ServerEntry(modifier: Modifier = Modifier, server: ServersUiState.Server) {
    Column(modifier) {
        Text("IP: ${server.serverUrl}")
        Text("Saved map url: ${server.savedMapUrl}")
        Text("Map url hint: ${server.mapUrlHint}")
    }
}

@Composable
fun ServersScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: ServersViewModel = koinViewModel()
) {
    TopScreen(navController, modifier) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    navController,
                    title = {
                        Text(stringResource(Res.string.config_servers_screen_title))
                    }
                )
            },
        ) { innerPadding ->
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LazyColumn(modifier = Modifier.padding(innerPadding).fillMaxSize()) {

                uiState.currentServer?.let { server ->
                    item { Text(stringResource(Res.string.config_servers_screen_currentServer)) }
                    item {
                        ServerEntry(
                            Modifier.clickable { navController.navigate(Screen.Server(server.serverId)) },
                            server
                        )
                    }
                }

                if (uiState.allServers.isNotEmpty()) {
                    item { Text(stringResource(Res.string.config_servers_screen_allServers)) }
                }

                items(uiState.allServers.size) { index ->

                }
            }
        }
    }
}