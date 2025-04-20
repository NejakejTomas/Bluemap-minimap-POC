package cz.nejakejtomas.bluemapminimap.usecase.mapurl

import cz.nejakejtomas.bluemapminimap.dbs.repository.ServerRepository
import cz.nejakejtomas.bluemapminimap.model.ServerId
import cz.nejakejtomas.bluemapminimap.runSuspendCatching
import io.ktor.http.*

class GetSavedMapUrlUseCase(
    private val serverRepository: ServerRepository,
) {
    suspend operator fun invoke(serverId: ServerId): Url? {
        return runSuspendCatching {
            URLBuilder(serverRepository.getMapUrl(serverId) ?: return null).build()
        }.getOrDefault(null)
    }
}