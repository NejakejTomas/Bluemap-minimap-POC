package cz.nejakejtomas.bluemapminimap.dbs.repository

import cz.nejakejtomas.bluemapminimap.dbs.dao.ServerDao
import cz.nejakejtomas.bluemapminimap.dbs.entity.Server
import cz.nejakejtomas.bluemapminimap.model.ServerId

class ServerRepository(
    private val serverDao: ServerDao,
) {
    suspend fun getMapUrl(server: ServerId): String? {
        return serverDao.select(server.url)?.mapUrl
    }

    suspend fun setMapUrl(server: ServerId, mapUrl: String?): Long {
        return serverDao.upsert(Server(0, server.url, mapUrl))
    }
}