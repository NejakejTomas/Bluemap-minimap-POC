package cz.nejakejtomas.bluemapminimap.screen

import cz.nejakejtomas.bluemapminimap.client.ServerClient
import cz.nejakejtomas.bluemapminimap.config.WorldConfig
import cz.nejakejtomas.bluemapminimap.config.WorldDefaults
import cz.nejakejtomas.bluemapminimap.koin.ScopeManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import me.shedaniel.clothconfig2.api.ConfigBuilder
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import org.koin.core.scope.Scope

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
            .createServer()
            .createGeneral()
            .createWorld()

        return builder.build()
    }

    private fun ConfigBuilder.createGeneral(): ConfigBuilder {
        title = Component.translatable("bluemapminimap.screen.config.config")
        val category = getOrCreateCategory(Component.translatable("bluemapminimap.screen.config.general.general"))

        return this
    }

    private fun ConfigBuilder.createServer(): ConfigBuilder {

        return this
    }

    private fun ConfigBuilder.createWorld(): ConfigBuilder {
        val world = currentWorld ?: return this
        val server = currentServer ?: return this

        val category = getOrCreateCategory(Component.translatable("bluemapminimap.screen.config.dimension.world"))

        val entryBuilder = entryBuilder()
            .startStringDropdownMenu(Component.translatable("bluemapminimap.screen.config.dimension.mapLocation"), world.config.getMapName() ?: "")
            .setTooltip(Component.translatable("bluemapminimap.screen.config.dimension.mapLocation.tooltip"))
            .setSelections(
                if (server.allMaps.isCompleted) runBlocking { server.allMaps.await() }
                else listOf()
            )
            .setDefaultValue {
                if (world.defaultMapName.isCompleted) runBlocking { world.defaultMapName.await() }
                else ""
            }
            .setSaveConsumer {
                world.config.setMapName(it)
                scopeManager.configurationChange()
            }

        category.addEntry(entryBuilder.build())

        return this
    }
}