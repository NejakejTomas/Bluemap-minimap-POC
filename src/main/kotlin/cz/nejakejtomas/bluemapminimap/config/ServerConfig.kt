package cz.nejakejtomas.bluemapminimap.config

import cz.nejakejtomas.bluemapminimap.dbs.repository.ServerRepository
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ServerConfig(private val serverRepository: ServerRepository, private val serverDefaults: ServerDefaults) {
    suspend fun getSavedMapUrl(): Url? {
        try {
            return URLBuilder(serverRepository.getMapUrl() ?: return null).build()
        } catch (_: Exception) {
        }
        return null
    }

    suspend fun getMapUrlOrDefault(): Url? = withContext(Dispatchers.IO) {
        return@withContext getSavedMapUrl() ?: serverDefaults.getMapUrl()
    }

    suspend fun setMapUrl(url: Url?) {
        serverRepository.setMapUrl(url?.toString())
    }
}