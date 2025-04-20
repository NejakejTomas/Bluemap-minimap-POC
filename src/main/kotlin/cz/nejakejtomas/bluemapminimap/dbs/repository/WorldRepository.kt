package cz.nejakejtomas.bluemapminimap.dbs.repository

import androidx.room.immediateTransaction
import androidx.room.useWriterConnection
import cz.nejakejtomas.bluemapminimap.dbs.AppDatabase
import cz.nejakejtomas.bluemapminimap.dbs.dao.ServerDao
import cz.nejakejtomas.bluemapminimap.dbs.dao.WorldDao
import cz.nejakejtomas.bluemapminimap.dbs.entity.Server
import cz.nejakejtomas.bluemapminimap.dbs.entity.World
import cz.nejakejtomas.bluemapminimap.model.ServerId
import cz.nejakejtomas.bluemapminimap.model.WorldId

class WorldRepository(
    private val serverDao: ServerDao,
    private val worldDao: WorldDao,
    private val database: AppDatabase,
) {
    suspend fun getMapName(server: ServerId, world: WorldId): String? {
        return worldDao.select(world.dimension, server.url)?.mapName
    }

    suspend fun setMapName(server: ServerId, world: WorldId, mapName: String?) {
        database.useWriterConnection { transactor ->
            transactor.immediateTransaction {
                val serverEntity = serverDao.select(server.url)
                val serverId = serverEntity?.id ?: serverDao.upsert(Server(0, server.url, null))

                val worldEntity = worldDao.select(world.dimension, server.url)?.copy(mapName = mapName) ?: World(
                    0,
                    serverId,
                    world.dimension,
                    mapName
                )

                worldDao.upsert(worldEntity)
            }
        }
    }
}