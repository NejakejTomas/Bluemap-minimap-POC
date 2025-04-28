package cz.nejakejtomas.bluemapminimap.usecase.mapname

import cz.nejakejtomas.bluemapminimap.client.server.ServerApiFactory
import cz.nejakejtomas.bluemapminimap.model.MapDimensionId
import cz.nejakejtomas.bluemapminimap.model.ServerId
import cz.nejakejtomas.bluemapminimap.model.WorldId
import cz.nejakejtomas.bluemapminimap.usecase.mapurl.GetSavedOrGuessMapUrlUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Assumes correct map url, returns string - map name
class GuessMapNameUseCase(
    private val serverApiFactory: ServerApiFactory,
    private val getSavedOrGuessMapUrlUseCase: GetSavedOrGuessMapUrlUseCase,
) {
    suspend operator fun invoke(serverId: ServerId, worldId: WorldId): MapDimensionId? = withContext(Dispatchers.IO) {
        val mapId = getSavedOrGuessMapUrlUseCase(serverId) ?: return@withContext null
        val client = serverApiFactory(mapId.mapUrl)

        val settings = client.settings().fold(onSuccess = { it }, onFailure = { return@withContext null })

        val name = worldId.dimension.process(DIMENSION_DELIMITERS).toSet()
        val availableMaps = settings.maps.map { it.process(MAPS_DELIMITERS).toSet() to it }

        val bestMap = availableMaps.map { map ->
            val totalWordCount = name.union(map.first).size
            val sameWordCount = name.intersect(map.first).size

            totalWordCount - sameWordCount to map.second
        }.sortedBy { it.first }.map { it.second }.firstOrNull()?.let { MapDimensionId(it) }

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