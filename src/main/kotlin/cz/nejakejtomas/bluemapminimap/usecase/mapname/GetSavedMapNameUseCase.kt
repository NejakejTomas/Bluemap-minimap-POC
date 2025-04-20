package cz.nejakejtomas.bluemapminimap.usecase.mapname

import cz.nejakejtomas.bluemapminimap.dbs.repository.WorldRepository
import cz.nejakejtomas.bluemapminimap.model.ServerId
import cz.nejakejtomas.bluemapminimap.model.WorldId

class GetSavedMapNameUseCase(
    private val wordRepository: WorldRepository,
) {
    suspend operator fun invoke(serverId: ServerId, worldId: WorldId): String? {
        return wordRepository.getMapName(serverId, worldId)
    }
}