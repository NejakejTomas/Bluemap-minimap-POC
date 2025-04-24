package cz.nejakejtomas.bluemapminimap.usecase.mapname

import cz.nejakejtomas.bluemapminimap.dbs.repository.WorldRepository
import cz.nejakejtomas.bluemapminimap.model.MapDimensionId
import cz.nejakejtomas.bluemapminimap.model.ServerId
import cz.nejakejtomas.bluemapminimap.model.WorldId

class SaveMapNameUseCase(
    private val wordRepository: WorldRepository
) {
    suspend operator fun invoke(serverId: ServerId, worldId: WorldId, mapDimensionId: MapDimensionId?) {
        wordRepository.setMapName(serverId, worldId, mapDimensionId)
    }
}