package cz.nejakejtomas.bluemapminimap.screen.minimap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.nejakejtomas.bluemapminimap.dbs.repository.MinimapSettingsRepository
import cz.nejakejtomas.bluemapminimap.model.ServerId
import cz.nejakejtomas.bluemapminimap.model.WorldId
import cz.nejakejtomas.bluemapminimap.repository.MinecraftRepository
import cz.nejakejtomas.bluemapminimap.usecase.mapname.GetSavedOrGuessMapNameUseCase
import cz.nejakejtomas.bluemapminimap.usecase.maproot.GetMapRootUseCase
import cz.nejakejtomas.bluemapminimap.usecase.mapurl.GetSavedOrGuessMapUrlUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MinimapViewModel(
    minecraftRepository: MinecraftRepository,
    minimapSettingsRepository: MinimapSettingsRepository,
    private val getSavedOrGuessMapUrlUseCase: GetSavedOrGuessMapUrlUseCase,
    private val getSavedOrGuessMapNameUseCase: GetSavedOrGuessMapNameUseCase,
    private val getMapRootUseCase: GetMapRootUseCase,
) : ViewModel() {
//    private val job = Job()
//    private val viewModelScope = CoroutineScope(job + Dispatchers.Default)

//    private val s = CoroutineScope(viewModelScope.coroutineContext + SupervisorJob() + Dispatchers.Default)

    private val _uiState = MutableStateFlow(MinimapUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                minecraftRepository.server.filterNotNull(),
                minecraftRepository.world.filterNotNull(),
                ::resolveMapApiParameters
            ).collect()
        }

        viewModelScope.launch {
            minimapSettingsRepository.select().collect { settings ->
                _uiState.update {
                    it.copy(
                        doRotate = settings.doRotate,
                        debugRender = settings.debugRender,
                        targetBlockSize = settings.targetBlockSize
                    )
                }
            }
        }
    }

    private suspend fun resolveMapApiParameters(server: ServerId, world: WorldId): Unit {
        val mapUrl = getSavedOrGuessMapUrlUseCase(server)
        val mapName = getSavedOrGuessMapNameUseCase(server, world)
        val mapRoot = getMapRootUseCase(server)

        _uiState.update { it.copy(mapId = mapUrl, mapRoot = mapRoot, mapDimensionId = mapName) }
    }
}