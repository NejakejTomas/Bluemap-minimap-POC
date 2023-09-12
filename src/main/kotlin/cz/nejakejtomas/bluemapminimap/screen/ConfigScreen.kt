package cz.nejakejtomas.bluemapminimap.screen

import cz.nejakejtomas.bluemapminimap.client.ServerClient
import cz.nejakejtomas.bluemapminimap.config.ServerConfig
import cz.nejakejtomas.bluemapminimap.config.ServerDefaults
import cz.nejakejtomas.bluemapminimap.config.WorldConfig
import cz.nejakejtomas.bluemapminimap.config.WorldDefaults
import cz.nejakejtomas.bluemapminimap.koin.ScopeManager
import cz.nejakejtomas.bluemapminimap.urlFromUser
import cz.nejakejtomas.bluemapminimap.valueOrNull
import io.ktor.http.*
import kotlinx.coroutines.*
import me.shedaniel.clothconfig2.api.ConfigBuilder
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import org.koin.core.scope.Scope
import java.util.*

class ConfigScreen(private val scopeManager: ScopeManager, private val coroutineScope: CoroutineScope) {
    // TODO: Save on change
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
        category.addEntry(
            BooleanListEntry(
                Component.literal("Bool switch"),
                coroutineScope.async {
                    delay(2500)
                    false
                },
                Component.literal("Res"),
                {
                    coroutineScope.async {
                        delay(5000)
                        false
                    }
                },
                { /*Save*/ },
                { Optional.of(arrayOf(Component.literal("Tooltip"))) },
                false
            )
        )

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
            .setErrorSupplier {
                if (it.isEmpty()) return@setErrorSupplier Optional.empty()
                if (urlFromUser(it) == null) {
                    Optional.of(Component.translatable("bluemapminimap.screen.config.server.mapUrl.malformed"))
                } else {
                    Optional.empty()
                }
            }
            .setSaveConsumer {
                val url = urlFromUser(it)
                server.config.setMapUrl(url)
                scopeManager.configurationChange()
            }

        server.defaultMapUrl.ifCompleted { it?.let { entryBuilder.setDefaultValue(it.toString()) } }

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
            .setErrorSupplier {
                if (it.isEmpty()) return@setErrorSupplier Optional.empty()
                if (!server.allMaps.isCompleted) return@setErrorSupplier Optional.empty()

                val allMaps = runBlocking { server.allMaps.await() } ?: return@setErrorSupplier Optional.empty()

                if (allMaps.contains(it)) return@setErrorSupplier Optional.empty()

                return@setErrorSupplier Optional.of(Component.translatable("bluemapminimap.screen.config.dimension.mapName.tooltip"))
            }
            .setSaveConsumer {
                world.config.setMapName(it.ifEmpty { null })
                scopeManager.configurationChange()
            }

        // TODO: Try to get cloth config to accept CompletableFuture or at least
        // evaluate suppliers lazily so this is not needed
        server.allMaps.ifCompleted { entryBuilder.setSelections(it) }
        world.defaultMapName.ifCompleted { entryBuilder.setDefaultValue(it) }

        category.addEntry(entryBuilder.build())

        return this
    }
}

private fun <T> Deferred<T>.ifCompleted(action: (T) -> Unit) {
    val x = valueOrNull()
    if (x != null) action(x)
}