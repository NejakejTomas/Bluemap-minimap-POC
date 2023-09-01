package cz.nejakejtomas.bluemapminimap.screen

import cz.nejakejtomas.bluemapminimap.client.ServerClient
import cz.nejakejtomas.bluemapminimap.config.ServerConfig
import cz.nejakejtomas.bluemapminimap.config.ServerDefaults
import cz.nejakejtomas.bluemapminimap.config.WorldConfig
import cz.nejakejtomas.bluemapminimap.config.WorldDefaults
import cz.nejakejtomas.bluemapminimap.koin.ScopeManager
import cz.nejakejtomas.bluemapminimap.urlFromUser
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import me.shedaniel.clothconfig2.api.ConfigBuilder
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import org.koin.core.scope.Scope
import java.util.*

class ConfigScreen(private val scopeManager: ScopeManager, private val coroutineScope: CoroutineScope) {
    private var currentWorld: World? = null
    private var currentServer: Server? = null

    inner class World(scope: Scope) {
        val config: WorldConfig = scope.get()
        val defaultMapName: Deferred<String> = coroutineScope.async {
            scope.get<WorldDefaults>().getMapName()
        }
    }

    inner class Server(scope: Scope) {
        val config: ServerConfig = scope.get()
        val defaultMapUrl: Deferred<Url?> = coroutineScope.async {
            scope.get<ServerDefaults>().getMapUrl()
        }
        val allMaps: Deferred<List<String>?> = coroutineScope.async {
            scope.get<ServerClient>().maps()
        }
    }

    init {
        scopeManager.newWorldScope.register {
            currentWorld = it?.let { World(it) }
        }

        scopeManager.newServerScope.register {
            currentServer = it?.let { Server(it) }
        }
    }

    fun create(parent: Screen): Screen {
        val builder = ConfigBuilder.create()
            .setTitle(Component.translatable("bluemapminimap.screen.config.config"))
            .setParentScreen(parent)
            .setShouldListSmoothScroll(false)
            .setShouldTabsSmoothScroll(false)
            .alwaysShowTabs()
            .transparentBackground()
            .createGeneral()
            .createServer()
            .createWorld()

        return builder.build()
    }

    private fun ConfigBuilder.createGeneral(): ConfigBuilder {
        title = Component.translatable("bluemapminimap.screen.config.config")
        val category = getOrCreateCategory(Component.translatable("bluemapminimap.screen.config.general.general"))

        return this
    }

    private fun ConfigBuilder.createServer(): ConfigBuilder {
        val server = currentServer ?: return this
        val category = getOrCreateCategory(Component.translatable("bluemapminimap.screen.config.server.server"))

        val entryBuilder = entryBuilder()
            .startTextField(
                Component.translatable("bluemapminimap.screen.config.server.mapUrl"),
                server.config.getSavedMapUrl()?.toString() ?: ""
            )
            .setTooltip(Component.translatable("bluemapminimap.screen.config.server.mapUrl.tooltip"))
            .setDefaultValue {
                if (server.defaultMapUrl.isCompleted) runBlocking { server.defaultMapUrl.await()?.toString() ?: "" }
                else ""
            }
            .setErrorSupplier {
                if (it.isEmpty()) return@setErrorSupplier Optional.empty()
                if (urlFromUser(it) == null) {
                    Optional.of(Component.translatable("bluemapminimap.screen.config.server.mapUrl.malformed"))
                } else {
                    Optional.empty()
                }
            }
            .setSaveConsumer {
                val url = urlFromUser(it)!!
                server.config.setMapUrl(url)
                scopeManager.configurationChange()
            }

        category.addEntry(entryBuilder.build())

        return this
    }

    private fun ConfigBuilder.createWorld(): ConfigBuilder {
        val server = currentServer ?: return this
        val world = currentWorld ?: return this

        val category = getOrCreateCategory(Component.translatable("bluemapminimap.screen.config.dimension.dimension"))

        val entryBuilder = entryBuilder()
            .startStringDropdownMenu(
                Component.translatable("bluemapminimap.screen.config.dimension.mapName"),
                world.config.getSavedMapName() ?: ""
            )
            .setTooltip(Component.translatable("bluemapminimap.screen.config.dimension.mapName.tooltip"))
            .setSelections(
                if (server.allMaps.isCompleted) runBlocking { server.allMaps.await() }
                else listOf()
            )
            .setErrorSupplier {
                if (it.isEmpty()) return@setErrorSupplier Optional.empty()
                if (!server.allMaps.isCompleted) return@setErrorSupplier Optional.empty()

                val allMaps = runBlocking { server.allMaps.await() } ?: return@setErrorSupplier Optional.empty()

                if (allMaps.contains(it)) return@setErrorSupplier Optional.empty()

                return@setErrorSupplier Optional.of(Component.translatable("bluemapminimap.screen.config.dimension.mapName.tooltip"))
            }
            .setDefaultValue {
                if (world.defaultMapName.isCompleted) runBlocking { world.defaultMapName.await() }
                else ""
            }
            .setSaveConsumer {
                world.config.setMapName(it.ifEmpty { null })
                scopeManager.configurationChange()
            }

        category.addEntry(entryBuilder.build())

        return this
    }
}