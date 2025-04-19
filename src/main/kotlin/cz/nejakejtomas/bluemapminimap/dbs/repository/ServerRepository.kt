package cz.nejakejtomas.bluemapminimap.dbs.repository

import cz.nejakejtomas.bluemapminimap.ServerId
import cz.nejakejtomas.bluemapminimap.dbs.dao.ServerDao
import cz.nejakejtomas.bluemapminimap.dbs.entity.Server

class ServerRepository(
    private val server: ServerId,
    private val serverDao: ServerDao,
) {
    suspend fun getMapUrl(): String? {
        return serverDao.select(server.url)?.mapUrl
    }

    suspend fun setMapUrl(mapUrl: String?) {
        serverDao.upsert(Server(0, server.url, mapUrl))
    }
}