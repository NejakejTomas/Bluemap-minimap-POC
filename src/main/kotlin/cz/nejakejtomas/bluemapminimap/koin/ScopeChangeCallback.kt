package cz.nejakejtomas.bluemapminimap.koin

import org.koin.core.scope.Scope

fun interface ScopeChangeCallback {
    fun newScope(scope: Scope?)
}