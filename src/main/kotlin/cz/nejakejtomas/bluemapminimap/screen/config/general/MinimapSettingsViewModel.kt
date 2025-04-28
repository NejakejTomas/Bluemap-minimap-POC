package cz.nejakejtomas.bluemapminimap.screen.config.general

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.nejakejtomas.bluemapminimap.common.Size
import cz.nejakejtomas.bluemapminimap.dbs.entity.MinimapSettings
import cz.nejakejtomas.bluemapminimap.dbs.repository.MinimapSettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MinimapSettingsViewModel(
    private val minimapSettingsRepository: MinimapSettingsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(MinimapSettings().toUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val settings = minimapSettingsRepository.selectOne()
            _uiState.value = settings.toUiState()

            uiState.collect(::updateDatabase)
        }
    }

    fun debugRenderChanged(value: Boolean) {
        _uiState.update { it.copy(debugRender = value) }
    }

    fun doRotateChanged(value: Boolean) {
        _uiState.update { it.copy(doRotate = value) }
    }

    fun blockSizeChanged(value: String) {
        val isError = value.toIntOrNull() == null

        _uiState.update {
            it.copy(
                targetSizeBlocks = value,
                targetSizeBlocksError = isError
            )
        }
    }

    private fun MinimapSettings.toUiState() = MinimapSettingsUiState(
        doRotate = doRotate,
        debugRender = debugRender,
        targetSizeBlocks = targetBlockSize.width.toString(),
        targetSizeBlocksError = false
    )

    private suspend fun updateDatabase(state: MinimapSettingsUiState) {
        val blockSize = state.targetSizeBlocks.toIntOrNull()
        minimapSettingsRepository.update {
            it.copy(
                doRotate = state.doRotate,
                debugRender = state.debugRender,
                targetBlockSize = blockSize?.let { s -> Size(s, s) } ?: it.targetBlockSize
            )
        }
    }
}