package cz.nejakejtomas.bluemapminimap.config

import cz.nejakejtomas.bluemapminimap.World
import cz.nejakejtomas.bluemapminimap.client.ServerClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WorldDefaults(private val world: World, private val serverClient: ServerClient) {
    suspend fun getMapName(): String = withContext(Dispatchers.IO) {
        val name = world.dimension.process(DIMENSION_DELIMITERS).toSet()
        val availableMaps =
            serverClient.maps()?.map { it.process(MAPS_DELIMITERS).toSet() to it } ?: return@withContext ""

        val bestMap = availableMaps.map { map ->
            val totalWordCount = name.union(map.first).size
            val sameWordCount = name.intersect(map.first).size

            totalWordCount - sameWordCount to map.second
        }.minByOrNull { it.first }?.second ?: ""

        return@withContext bestMap
    }

    companion object {
        private const val DIMENSION_DELIMITERS = "_.-:/"
        private const val MAPS_DELIMITERS = "_.-:/ "

        private val String.splitByCamelCase: List<String>
            get() {
                return this.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])")
            }

        private fun String.splitByDelimiters(delimiters: String): List<String> {
                return this.split(*delimiters.toCharArray())
            }

        private fun String.process(delimiters: String): List<String> {
                return this.splitByCamelCase.flatMap { byCamelCase ->
                    byCamelCase.splitByDelimiters(delimiters)
                }.map { it.lowercase() }
            }
    }
}