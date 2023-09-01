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
import kotlinx.coroutines.runBlocking
import net.minecraft.client.Minecraft
import org.koin.core.context.startKoin
import org.koin.dsl.module

object Koin {
    private object Modules {
        fun global() = module {
            single<Minecraft> { Minecraft.getInstance() }
            single<CoroutineDispatcher>(TickDispatcher) { TickDispatcher() }
            single<CoroutineDispatcher>(GuiRenderDispatcher) { GuiRenderDispatcher() }
            single<Database> { Database }
            single<ScopeManager>(createdAtStart = true) { ScopeManager() }
            single<ConfigScreen>(createdAtStart = true) { ConfigScreen }

            single<GuiRenderable> { Minimap() }
        }

        fun server() = module {
            scope(LifecycleQualifier.Server) {
                scoped<Server> { Server(id) }
                scoped<ServerClient> { ServerClientImpl() }
                scoped<ServerDao> { ServerDao(get(), get()) }
                scoped<ServerDefaults> { ServerDefaults(get()) }
                scoped<ServerConfig> { ServerConfig(get(), get()) }
            }
        }

        fun world() = module {
            scope(LifecycleQualifier.World) {
                scoped<World> { World(id) }
                scoped<WorldDao> { WorldDao(get(), get(), get()) }
                scoped<WorldDefaults> { WorldDefaults(get(), get()) }
                scoped<WorldConfig> { WorldConfig(get(), get()) }

                scoped<MapClient> {
                    val config: WorldConfig = get()

                    val name = runBlocking {
                        config.getMapNameOrDefault()
                    }

                    MapClientImpl(name)
                }

                scoped<TileMap> {
                    TileMap(
                        get(),
                        get(),
                        get(GuiRenderDispatcher),
                        get(TickDispatcher)
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