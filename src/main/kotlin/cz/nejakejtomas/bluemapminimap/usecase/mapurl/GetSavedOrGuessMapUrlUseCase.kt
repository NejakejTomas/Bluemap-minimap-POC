package cz.nejakejtomas.bluemapminimap.usecase.mapurl

import cz.nejakejtomas.bluemapminimap.dbs.repository.ServerRepository
import cz.nejakejtomas.bluemapminimap.model.MapId
import cz.nejakejtomas.bluemapminimap.model.ServerId
import cz.nejakejtomas.bluemapminimap.runSuspendCatching

class GetSavedOrGuessMapUrlUseCase(
    private val serverRepository: ServerRepository,
    private val guessMapUrlUseCase: GuessMapUrlUseCase,
) {
    suspend operator fun invoke(serverId: ServerId): MapId? {
        val mapUrl = runSuspendCatching {
            serverRepository.getMapUrl(serverId)
        }.getOrNull()?.let { MapId(it) }

        return mapUrl ?: return guessMapUrlUseCase(serverId)
    }
}