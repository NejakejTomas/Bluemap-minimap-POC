package cz.nejakejtomas.bluemapminimap.screen.minimap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.nejakejtomas.bluemapminimap.repository.MinecraftRepository
import cz.nejakejtomas.bluemapminimap.usecase.mapname.GetSavedOrDefaultMapNameUseCase
import cz.nejakejtomas.bluemapminimap.usecase.mapurl.GetSavedOrDefaultMapUrlUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class MinimapViewModel(
    minecraftRepository: MinecraftRepository,
    private val getSavedOrDefaultMapUrlUseCase: GetSavedOrDefaultMapUrlUseCase,
    private val getSavedOrDefaultMapNameUseCase: GetSavedOrDefaultMapNameUseCase,
) : ViewModel() {
//    private val job = Job()
//    private val viewModelScope = CoroutineScope(job + Dispatchers.Default)

    private val s = CoroutineScope(viewModelScope.coroutineContext + SupervisorJob() + Dispatchers.Default)

    val uiState = combine(minecraftRepository.server, minecraftRepository.world) { server, world ->
        val mapUrl = server?.let {
            getSavedOrDefaultMapUrlUseCase(it)
        }
        val mapName = world?.let { w ->
            server?.let { s ->
                getSavedOrDefaultMapNameUseCase(s, w)
            }
        }

        MinimapUiState(mapUrl, mapName)
    }.stateIn(s, SharingStarted.Eagerly, MinimapUiState())
}