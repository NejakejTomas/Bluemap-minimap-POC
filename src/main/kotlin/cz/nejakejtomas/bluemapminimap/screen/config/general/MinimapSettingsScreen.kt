package cz.nejakejtomas.bluemapminimap.screen.config.general

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import cz.nejakejtomas.bluemapminimap.screen.config.TopAppBar
import cz.nejakejtomas.bluemapminimap.screen.config.TopScreen
import cz.nejakejtomas.minimap.resources.*
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
private fun DebugRenderer(
    value: Boolean,
    onValueChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(stringResource(Res.string.config_minimap_screen_debugRender))
        Spacer(Modifier.weight(1f))
        Switch(value, onValueChange)
    }
}

@Composable
private fun DoRotate(
    value: Boolean,
    onValueChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(stringResource(Res.string.config_minimap_screen_doRotate))
        Spacer(Modifier.weight(1f))
        Switch(value, onValueChange)
    }
}

@Composable
private fun BlockSize(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    modifier: Modifier = Modifier
) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(stringResource(Res.string.config_minimap_screen_sizeInBlocks))
        Spacer(Modifier.weight(1f))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            isError = isError
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MinimapSettingsScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: MinimapSettingsViewModel = koinViewModel()
) {
    TopScreen(navController, modifier) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    navController,
                    title = {
                        Text(stringResource(Res.string.config_minimap_screen_title))
                    }
                )
            }
        ) { innerPadding ->
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            Column(Modifier.padding(innerPadding).fillMaxWidth().verticalScroll(rememberScrollState())) {
                DebugRenderer(uiState.debugRender, viewModel::debugRenderChanged)
                DoRotate(uiState.doRotate, viewModel::doRotateChanged)
                BlockSize(uiState.targetSizeBlocks, viewModel::blockSizeChanged, uiState.targetSizeBlocksError)
            }
        }
    }

}