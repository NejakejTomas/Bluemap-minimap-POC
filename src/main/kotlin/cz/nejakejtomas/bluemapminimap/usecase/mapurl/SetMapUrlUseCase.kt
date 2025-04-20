package cz.nejakejtomas.bluemapminimap.usecase.mapurl

import cz.nejakejtomas.bluemapminimap.dbs.repository.ServerRepository
import cz.nejakejtomas.bluemapminimap.model.ServerId
import io.ktor.http.*

class SetMapUrlUseCase(
    private val serverRepository: ServerRepository
) {
    suspend operator fun invoke(serverId: ServerId, url: Url?) {
        serverRepository.setMapUrl(serverId, url?.toString())
    }
}