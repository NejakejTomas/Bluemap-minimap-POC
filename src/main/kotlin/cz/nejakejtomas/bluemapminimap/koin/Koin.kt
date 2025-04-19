package cz.nejakejtomas.bluemapminimap.koin

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import cz.nejakejtomas.bluemapminimap.ServerId
import cz.nejakejtomas.bluemapminimap.WorldId
import cz.nejakejtomas.bluemapminimap.client.MapClient
import cz.nejakejtomas.bluemapminimap.client.MapClientImpl
import cz.nejakejtomas.bluemapminimap.client.ServerClient
import cz.nejakejtomas.bluemapminimap.client.ServerClientImpl
import cz.nejakejtomas.bluemapminimap.config.*
import cz.nejakejtomas.bluemapminimap.dbs.AppDatabase
import cz.nejakejtomas.bluemapminimap.dbs.dao.ServerDao
import cz.nejakejtomas.bluemapminimap.dbs.dao.WorldDao
import cz.nejakejtomas.bluemapminimap.dbs.repository.ServerRepository
import cz.nejakejtomas.bluemapminimap.dbs.repository.WorldRepository
import cz.nejakejtomas.bluemapminimap.mc.GuiRenderDispatcher
import cz.nejakejtomas.bluemapminimap.mc.TickDispatcher
import cz.nejakejtomas.bluemapminimap.render.GuiRenderable
import cz.nejakejtomas.bluemapminimap.render.Minimap
import cz.nejakejtomas.bluemapminimap.render.NewNewTileMap
import cz.nejakejtomas.bluemapminimap.render.TileMap
import cz.nejakejtomas.bluemapminimap.screen.ConfigScreen
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import net.minecraft.client.Minecraft
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.*
import org.koin.dsl.module

object Koin {
    private object Modules {
        fun global() = module {
            single<Minecraft> { Minecraft.getInstance() }
            single<CoroutineScope>(LifecycleQualifier.Global) { CoroutineScope(SupervisorJob()) }
            single<CoroutineScope> { get(LifecycleQualifier.Global) }
            single<CoroutineDispatcher>(TickDispatcher) { TickDispatcher() }
            single<CoroutineDispatcher>(GuiRenderDispatcher) { GuiRenderDispatcher() }
            singleOf(::DebugConfig)
            single<ScopeManager> { ScopeManager() } withOptions { createdAtStart() }
            singleOf(::ConfigScreen) withOptions { createdAtStart() }
            singleOf(::Minimap) { bind<GuiRenderable>() }

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
        }

        fun server() = module {
            scope(LifecycleQualifier.Server) {
                scoped<CoroutineScope>(LifecycleQualifier.Server) { CoroutineScope(SupervisorJob()) }
                scoped<CoroutineScope> { get(LifecycleQualifier.Server) }
                scoped<ServerId> { ServerId(id) }
                scoped<ServerClient> { ServerClientImpl(get()) }
//                scopedOf(::ServerDao)
                scopedOf(::ServerRepository)
                scopedOf(::ServerDefaults)
                scopedOf(::ServerConfig)
            }
        }

        fun world() = module {
            scope(LifecycleQualifier.World) {
                scoped<CoroutineScope>(LifecycleQualifier.World) { CoroutineScope(SupervisorJob()) }
                scoped<CoroutineScope> { get(LifecycleQualifier.World) }
                scoped<WorldId> { WorldId(id) }
//                scopedOf(::WorldDao)
                scopedOf(::WorldRepository)
                scopedOf(::WorldDefaults)
                scopedOf(::WorldConfig)

                scoped<MapClient> { MapClientImpl(get(), get()) }

//                scoped<TileMap> {
//                    OldTileMap(
//                        get(),
//                        get(),
//                        get(GuiRenderDispatcher),
//                        get(TickDispatcher),
//                        get()
//                    )
//                }

                scoped<TileMap> { params ->
                    NewNewTileMap(
                        get(),
                        params.get(),
                        params.get(),
                        get(),
                        get(GuiRenderDispatcher),
                        get(TickDispatcher),
                        get(),
                        get()
                    )
                }
            }
        }
    }

    fun start() {
        startKoin {
            modules(Modules.global(), Modules.server(), Modules.world())
        }
    }
}