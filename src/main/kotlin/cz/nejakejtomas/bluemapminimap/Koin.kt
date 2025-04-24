package cz.nejakejtomas.bluemapminimap

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import cz.nejakejtomas.bluemapminimap.client.map.MapApiClient
import cz.nejakejtomas.bluemapminimap.config.DebugConfig
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
import cz.nejakejtomas.bluemapminimap.usecase.mapname.GetSavedOrGuessMapNameUseCase
import cz.nejakejtomas.bluemapminimap.usecase.mapname.GuessMapNameUseCase
import cz.nejakejtomas.bluemapminimap.usecase.mapname.SaveMapNameUseCase
import cz.nejakejtomas.bluemapminimap.usecase.maproot.GetMapRootUseCase
import cz.nejakejtomas.bluemapminimap.usecase.mapurl.GetSavedMapUrlUseCase
import cz.nejakejtomas.bluemapminimap.usecase.mapurl.GetSavedOrGuessMapUrlUseCase
import cz.nejakejtomas.bluemapminimap.usecase.mapurl.GuessMapUrlUseCase
import cz.nejakejtomas.bluemapminimap.usecase.mapurl.SaveMapUrlUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import net.minecraft.client.Minecraft
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
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
    // Map url
    singleOf(::GetSavedMapUrlUseCase)
    singleOf(::GuessMapUrlUseCase)
    singleOf(::GetSavedOrGuessMapUrlUseCase)
    singleOf(::SaveMapUrlUseCase)

    // Map name
    singleOf(::GetSavedMapNameUseCase)
    singleOf(::GuessMapNameUseCase)
    singleOf(::GetSavedOrGuessMapNameUseCase)
    singleOf(::SaveMapNameUseCase)

    // Map root
    singleOf(::GetMapRootUseCase)

    single<CoroutineDispatcher>(TickDispatcher.Companion) { TickDispatcher() }
    single<CoroutineDispatcher>(GuiRenderDispatcher.Companion) { GuiRenderDispatcher() }

    single<TileMapFactory> {
        TileMapFactory { settings: TileMapSettings, mapApiClient: MapApiClient, coroutineScope: CoroutineScope ->
            TileMapImpl(
                minecraft = get(),
                settings = settings,
                coroutineScope = coroutineScope,
                renderDispatcher = get(GuiRenderDispatcher.Companion),
                mapApiClient = mapApiClient,
                debugConfig = get()
            )
        }
    }

    singleOf(::DebugConfig)
    single<GuiRenderable> { Minimap(get(), get(), get(), get()) }
}