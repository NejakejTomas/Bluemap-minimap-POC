package cz.nejakejtomas.bluemapminimap.screen.config

import cz.nejakejtomas.bluemapminimap.screen.config.general.MinimapSettingsViewModel
import cz.nejakejtomas.bluemapminimap.screen.config.server.ServerViewModel
import cz.nejakejtomas.bluemapminimap.screen.config.servers.ServersViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val configModule = module {
    viewModelOf(::MinimapSettingsViewModel)
    viewModelOf(::ServersViewModel)
    viewModelOf(::ServerViewModel)
}