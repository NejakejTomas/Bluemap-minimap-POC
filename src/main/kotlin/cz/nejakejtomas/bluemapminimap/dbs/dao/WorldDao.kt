package cz.nejakejtomas.bluemapminimap.dbs.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import cz.nejakejtomas.bluemapminimap.dbs.entity.World

@Dao
interface WorldDao {
    @Query(
        "SELECT * FROM world " +
                "INNER JOIN server ON server.id = world.serverId " +
                "WHERE world.dimension = :dimension AND server.url = :serverUrl " +
                "LIMIT 1"
    )
    suspend fun select(dimension: String, serverUrl: String): World?

    @Upsert
    suspend fun upsert(world: World): Long
}