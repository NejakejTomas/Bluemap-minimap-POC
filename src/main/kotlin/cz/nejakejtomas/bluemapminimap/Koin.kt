package cz.nejakejtomas.bluemapminimap

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import cz.nejakejtomas.bluemapminimap.client.MapClient
import cz.nejakejtomas.bluemapminimap.client.MapClientImpl
import cz.nejakejtomas.bluemapminimap.client.ServerClient
import cz.nejakejtomas.bluemapminimap.client.ServerClientImpl
import cz.nejakejtomas.bluemapminimap.config.DebugConfig
import cz.nejakejtomas.bluemapminimap.config.ServerDefaults
import cz.nejakejtomas.bluemapminimap.config.WorldDefaults
import cz.nejakejtomas.bluemapminimap.dbs.AppDatabase
import cz.nejakejtomas.bluemapminimap.dbs.dao.ServerDao
import cz.nejakejtomas.bluemapminimap.dbs.dao.WorldDao
import cz.nejakejtomas.bluemapminimap.dbs.repository.ServerRepository
import cz.nejakejtomas.bluemapminimap.dbs.repository.WorldRepository
import cz.nejakejtomas.bluemapminimap.mc.GuiRenderDispatcher
import cz.nejakejtomas.bluemapminimap.mc.TickDispatcher
import cz.nejakejtomas.bluemapminimap.render.*
import cz.nejakejtomas.bluemapminimap.repository.MinecraftRepository
import cz.nejakejtomas.bluemapminimap.screen.minimap.MinimapViewModel
import cz.nejakejtomas.bluemapminimap.usecase.mapname.GetSavedMapNameUseCase
import cz.nejakejtomas.bluemapminimap.usecase.mapname.GetSavedOrDefaultMapNameUseCase
import cz.nejakejtomas.bluemapminimap.usecase.mapname.SetMapNameUseCase
import cz.nejakejtomas.bluemapminimap.usecase.mapurl.GetSavedMapUrlUseCase
import cz.nejakejtomas.bluemapminimap.usecase.mapurl.GetSavedOrDefaultMapUrlUseCase
import cz.nejakejtomas.bluemapminimap.usecase.mapurl.SetMapUrlUseCase
import io.ktor.http.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import net.minecraft.client.Minecraft
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val module = module {
    // Minecraft
    singleOf(Minecraft::getInstance)

    // Utils
    singleOf(::GlobalViewModelStoreOwner)

    // Repositories
    singleOf(::MinecraftRepository)
    singleOf(::ServerRepository)
    singleOf(::WorldRepository)

    // Database
    single {
        Room.databaseBuilder<AppDatabase>(
            AppDatabase::class.java.simpleName
        ).apply {
            setDriver(BundledSQLiteDriver())
            setQueryCoroutineContext(Dispatchers.IO)
        }
            .build()
    }
    single<ServerDao> { get<AppDatabase>().serverDao() }
    single<WorldDao> { get<AppDatabase>().worldDao() }


    // ViewModels
    viewModelOf(::MinimapViewModel)


    // UseCases
    singleOf(::GetSavedMapNameUseCase)
    singleOf(::GetSavedOrDefaultMapNameUseCase)
    singleOf(::SetMapNameUseCase)
    singleOf(::GetSavedMapUrlUseCase)
    singleOf(::GetSavedOrDefaultMapUrlUseCase)
    singleOf(::SetMapUrlUseCase)



    single<CoroutineDispatcher>(TickDispatcher.Companion) { TickDispatcher() }
    single<CoroutineDispatcher>(GuiRenderDispatcher.Companion) { GuiRenderDispatcher() }
    factory<MapClient> { (mapUrl: Url?, mapName: String) ->
        MapClientImpl(mapUrl, mapName)
    }

    single<ServerClient> {
        ServerClientImpl(get())
    }

    factory<TileMap> { (settings: TileMapSettings, coroutineScope: CoroutineScope, mapUrl: Url?, mapName: String) ->
        TileMapImpl(
            get(),
            settings,
            coroutineScope,
            get(GuiRenderDispatcher.Companion),
            get(TickDispatcher.Companion),
            get { parametersOf(mapUrl, mapName) },
            get()
        )
    }

    singleOf(::ServerDefaults)
    singleOf(::WorldDefaults)
    singleOf(::DebugConfig)
    single<GuiRenderable> { Minimap(get(), get()) }
}