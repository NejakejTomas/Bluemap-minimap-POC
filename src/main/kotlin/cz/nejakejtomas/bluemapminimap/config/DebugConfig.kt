package cz.nejakejtomas.bluemapminimap.config

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DebugConfig {
    data class DebugConfigHolder(val debugRender: Boolean = true)

    private val _config = MutableStateFlow(DebugConfigHolder())
    val config: StateFlow<DebugConfigHolder>
        get() = _config

    fun setDebugRender(debugRender: Boolean) {
        _config.value = _config.value.copy(debugRender = debugRender)
    }
}
