package cz.nejakejtomas.bluemapminimap.usecase.mapname

import cz.nejakejtomas.bluemapminimap.dbs.repository.WorldRepository
import cz.nejakejtomas.bluemapminimap.model.ServerId
import cz.nejakejtomas.bluemapminimap.model.WorldId

class SetMapNameUseCase(
    private val wordRepository: WorldRepository
) {
    suspend operator fun invoke(serverId: ServerId, worldId: WorldId, name: String?) {
        wordRepository.setMapName(serverId, worldId, name)
    }
}