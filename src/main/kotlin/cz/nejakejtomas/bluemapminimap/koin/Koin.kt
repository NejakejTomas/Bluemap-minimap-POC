package cz.nejakejtomas.bluemapminimap.koin

import cz.nejakejtomas.bluemapminimap.Server
import cz.nejakejtomas.bluemapminimap.World
import cz.nejakejtomas.bluemapminimap.client.MapClient
import cz.nejakejtomas.bluemapminimap.client.MapClientImpl
import cz.nejakejtomas.bluemapminimap.client.ServerClient
import cz.nejakejtomas.bluemapminimap.client.ServerClientImpl
import cz.nejakejtomas.bluemapminimap.config.ServerConfig
import cz.nejakejtomas.bluemapminimap.config.ServerDefaults
import cz.nejakejtomas.bluemapminimap.config.WorldConfig
import cz.nejakejtomas.bluemapminimap.config.WorldDefaults
import cz.nejakejtomas.bluemapminimap.dbs.Database
import cz.nejakejtomas.bluemapminimap.dbs.ServerDao
import cz.nejakejtomas.bluemapminimap.dbs.WorldDao
import cz.nejakejtomas.bluemapminimap.mc.GuiRenderDispatcher
import cz.nejakejtomas.bluemapminimap.mc.TickDispatcher
import cz.nejakejtomas.bluemapminimap.render.GuiRenderable
import cz.nejakejtomas.bluemapminimap.render.Minimap
import cz.nejakejtomas.bluemapminimap.render.TileMap
import cz.nejakejtomas.bluemapminimap.screen.ConfigScreen
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import net.minecraft.client.Minecraft
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.scopedOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.withOptions
import org.koin.dsl.module

object Koin {
    private object Modules {
        fun global() = module {
            single<Minecraft> { Minecraft.getInstance() }
            single<CoroutineScope>(LifecycleQualifier.Global) { CoroutineScope(SupervisorJob()) }
            single<CoroutineScope> { get(LifecycleQualifier.Global) }
            single<CoroutineDispatcher>(TickDispatcher) { TickDispatcher() }
            single<CoroutineDispatcher>(GuiRenderDispatcher) { GuiRenderDispatcher() }
            single<Database> { Database }
            single<ScopeManager> { ScopeManager() } withOptions { createdAtStart() }
            singleOf(::ConfigScreen) withOptions { createdAtStart() }

            single<GuiRenderable> { Minimap(get(), get()) }
        }

        fun server() = module {
            scope(LifecycleQualifier.Server) {
                scoped<CoroutineScope>(LifecycleQualifier.Server) { CoroutineScope(SupervisorJob()) }
                scoped<CoroutineScope> { get(LifecycleQualifier.Server) }
                scoped<Server> { Server(id) }
                scoped<ServerClient> { ServerClientImpl(get()) }
                scopedOf(::ServerDao)
                scopedOf(::ServerDefaults)
                scopedOf(::ServerConfig)
            }
        }

        fun world() = module {
            scope(LifecycleQualifier.World) {
                scoped<CoroutineScope>(LifecycleQualifier.World) { CoroutineScope(SupervisorJob()) }
                scoped<CoroutineScope> { get(LifecycleQualifier.World) }
                scoped<World> { World(id) }
                scopedOf(::WorldDao)
                scopedOf(::WorldDefaults)
                scopedOf(::WorldConfig)

                scoped<MapClient> { MapClientImpl(get(), get()) }

                scoped<TileMap> {
                    TileMap(
                        get(),
                        get(),
                        get(GuiRenderDispatcher),
                        get(TickDispatcher),
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