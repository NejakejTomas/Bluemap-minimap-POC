package cz.nejakejtomas.bluemapminimap.config

import cz.nejakejtomas.bluemapminimap.dbs.ServerDao

class ServerConfig(private val serverDao: ServerDao, private val serverDefaults: ServerDefaults) {
    fun getMapUrl(): String? {
        return serverDao.getMapUrl()
    }

    fun getMapUrlOrDefault(): String {
        return getMapUrl() ?: serverDefaults.getMapUrl()
    }

    fun setMapUrl(url: String) {
        serverDao.setMapUrl(url)
    }
}