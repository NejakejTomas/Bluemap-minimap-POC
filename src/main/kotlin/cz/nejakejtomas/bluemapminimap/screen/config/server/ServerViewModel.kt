package cz.nejakejtomas.bluemapminimap.screen.config.server

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.nejakejtomas.bluemapminimap.model.ServerId
import cz.nejakejtomas.bluemapminimap.usecase.mapurl.GetSavedMapUrlUseCase
import cz.nejakejtomas.bluemapminimap.usecase.mapurl.GuessMapUrlUseCase
import cz.nejakejtomas.bluemapminimap.usecase.mapurl.SaveMapUrlUseCase
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ServerViewModel(
    private val serverId: ServerId,
    private val getSavedMapUrlUseCase: GetSavedMapUrlUseCase,
    private val guessMapUrlUseCase: GuessMapUrlUseCase,
    private val saveMapUrlUseCase: SaveMapUrlUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ServerUiState(serverId.url))
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            init()
        }
    }

    private suspend fun init(): Unit = coroutineScope {
        launch {
            getSavedMapUrlUseCase(serverId)?.mapUrl?.let { url ->
                _uiState.update { it.copy(savedMapUrl = url) }
            }
        }

        launch {
            guessMapUrlUseCase(serverId)?.mapUrl?.let { url ->
                _uiState.update { it.copy(mapUrlHint = url) }
            }
        }

    }
}