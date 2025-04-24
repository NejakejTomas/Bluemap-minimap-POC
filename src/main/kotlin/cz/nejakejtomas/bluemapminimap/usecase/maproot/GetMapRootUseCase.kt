package cz.nejakejtomas.bluemapminimap.usecase.maproot

import cz.nejakejtomas.bluemapminimap.client.server.ServerApiFactory
import cz.nejakejtomas.bluemapminimap.model.ServerId
import cz.nejakejtomas.bluemapminimap.usecase.mapurl.GetSavedOrGuessMapUrlUseCase

class GetMapRootUseCase(
    private val getSavedOrGuessMapUrlUseCase: GetSavedOrGuessMapUrlUseCase,
    private val serverApiFactory: ServerApiFactory,
) {
    suspend operator fun invoke(serverId: ServerId): String? {
        val mapId = getSavedOrGuessMapUrlUseCase(serverId) ?: return null
        val apiClient = serverApiFactory(mapId.mapUrl)
        val settings = apiClient.settings()

        return settings.map { it.dataRoot }.getOrNull()
    }
}