package cz.nejakejtomas.bluemapminimap.usecase.mapname

import cz.nejakejtomas.bluemapminimap.config.WorldDefaults
import cz.nejakejtomas.bluemapminimap.dbs.repository.WorldRepository
import cz.nejakejtomas.bluemapminimap.model.ServerId
import cz.nejakejtomas.bluemapminimap.model.WorldId

class GetSavedOrDefaultMapNameUseCase(
    private val wordRepository: WorldRepository,
    private val wordDefaults: WorldDefaults,
) {
    suspend operator fun invoke(serverId: ServerId, worldId: WorldId): String {
        return wordRepository.getMapName(serverId, worldId) ?: wordDefaults.getMapName(serverId, worldId)
    }
}