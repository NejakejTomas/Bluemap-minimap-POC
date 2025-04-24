package cz.nejakejtomas.bluemapminimap.screen.config.servers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.nejakejtomas.bluemapminimap.model.ServerId
import cz.nejakejtomas.bluemapminimap.repository.MinecraftRepository
import cz.nejakejtomas.bluemapminimap.usecase.mapurl.GetSavedMapUrlUseCase
import cz.nejakejtomas.bluemapminimap.usecase.mapurl.GetSavedOrGuessMapUrlUseCase
import cz.nejakejtomas.bluemapminimap.usecase.mapurl.SaveMapUrlUseCase
import io.ktor.http.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ServersViewModel(
    private val minecraftRepository: MinecraftRepository,
    private val getSavedMapUrlUseCase: GetSavedMapUrlUseCase,
    private val getSavedOrGuessMapUrlUseCase: GetSavedOrGuessMapUrlUseCase,
    private val saveMapUrlUseCase: SaveMapUrlUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ServersUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            minecraftRepository.server.collect { server ->
                if (server == null) return@collect

                _uiState.update {
                    it.copy(getServer(server))
                }
            }
        }
    }

    private suspend fun getServer(serverId: ServerId): ServersUiState.Server {
        // TODO: hint?
        val mapUrl = getSavedMapUrlUseCase(serverId)?.toString() ?: ""
//        val mapUrl = getSavedOrDefaultMapUrlUseCase(serverId)?.toString() ?: ""
        return ServersUiState.Server(serverId, mapUrl)
    }

    fun setMapUrl(url: Url) {
//        TODO
    }
}