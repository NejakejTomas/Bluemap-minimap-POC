package cz.nejakejtomas.bluemapminimap.usecase.mapurl

import cz.nejakejtomas.bluemapminimap.config.ServerDefaults
import cz.nejakejtomas.bluemapminimap.dbs.repository.ServerRepository
import cz.nejakejtomas.bluemapminimap.model.ServerId
import cz.nejakejtomas.bluemapminimap.runSuspendCatching
import io.ktor.http.*

class GetSavedOrDefaultMapUrlUseCase(
    private val serverRepository: ServerRepository,
    private val serverDefaults: ServerDefaults,
) {
    suspend operator fun invoke(serverId: ServerId): Url? {
        val mapUrl = runSuspendCatching {
            serverRepository.getMapUrl(serverId)?.let {
                URLBuilder(it).build()
            }
        }.getOrDefault(null)

        return mapUrl ?: return serverDefaults.getMapUrl(serverId)
    }
}