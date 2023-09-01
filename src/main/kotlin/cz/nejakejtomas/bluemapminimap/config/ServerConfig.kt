package cz.nejakejtomas.bluemapminimap.config

import cz.nejakejtomas.bluemapminimap.dbs.ServerDao
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ServerConfig(private val serverDao: ServerDao, private val serverDefaults: ServerDefaults) {
    fun getMapUrl(): Url? {
        try {
            return URLBuilder(serverDao.getMapUrl() ?: return null).build()
        } catch (_: Exception) {
        }
        return null
    }

    suspend fun getMapUrlOrDefault(): Url? = withContext(Dispatchers.IO) {
        return@withContext getMapUrl() ?: serverDefaults.getMapUrl()
    }

    fun setMapUrl(url: Url) {
        serverDao.setMapUrl(url.toString())
    }
}