package cz.nejakejtomas.bluemapminimap.render

import cz.nejakejtomas.bluemapminimap.client.map.MapApiClient
import kotlinx.coroutines.CoroutineScope

fun interface TileMapFactory {
    suspend operator fun invoke(
        settings: TileMapSettings,
        mapApiClient: MapApiClient,
        coroutineScope: CoroutineScope
    ): TileMap
}