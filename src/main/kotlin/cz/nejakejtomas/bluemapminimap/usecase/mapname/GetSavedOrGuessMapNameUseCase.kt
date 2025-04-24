package cz.nejakejtomas.bluemapminimap.usecase.mapname

import cz.nejakejtomas.bluemapminimap.dbs.repository.WorldRepository
import cz.nejakejtomas.bluemapminimap.model.MapDimensionId
import cz.nejakejtomas.bluemapminimap.model.ServerId
import cz.nejakejtomas.bluemapminimap.model.WorldId

class GetSavedOrGuessMapNameUseCase(
    private val worldRepository: WorldRepository,
    private val guessMapNameUseCase: GuessMapNameUseCase,
) {
    suspend operator fun invoke(serverId: ServerId, worldId: WorldId): MapDimensionId? {
        return worldRepository.getMapName(serverId, worldId) ?: guessMapNameUseCase(serverId, worldId)
    }
}