package cz.nejakejtomas.bluemapminimap.usecase.mapurl

import cz.nejakejtomas.bluemapminimap.dbs.repository.ServerRepository
import cz.nejakejtomas.bluemapminimap.model.MapId
import cz.nejakejtomas.bluemapminimap.model.ServerId

class SaveMapUrlUseCase(
    private val serverRepository: ServerRepository
) {
    suspend operator fun invoke(serverId: ServerId, mapId: MapId?) {
        serverRepository.setMapUrl(serverId, mapId?.mapUrl)
    }
}