package cz.nejakejtomas.bluemapminimap.screen.config

import cz.nejakejtomas.bluemapminimap.screen.config.general.GeneralViewModel
import cz.nejakejtomas.bluemapminimap.screen.config.servers.ServersViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val configModule = module {
    viewModelOf(::GeneralViewModel)
    viewModelOf(::ServersViewModel)
}