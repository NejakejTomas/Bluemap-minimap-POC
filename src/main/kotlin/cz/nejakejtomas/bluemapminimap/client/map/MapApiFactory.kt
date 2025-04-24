package cz.nejakejtomas.bluemapminimap.client.map

import cz.nejakejtomas.bluemapminimap.model.MapDimensionId
import cz.nejakejtomas.bluemapminimap.model.MapId

fun interface MapApiFactory {
    suspend operator fun invoke(mapId: MapId, mapRoot: String, mapDimensionId: MapDimensionId): MapApiClient
}