package cz.nejakejtomas.bluemapminimap.dbs.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import cz.nejakejtomas.bluemapminimap.dbs.entity.Server

@Dao
interface ServerDao {
    @Query("SELECT * FROM server WHERE url = :serverUrl LIMIT 1")
    suspend fun select(serverUrl: String): Server?

    @Upsert
    suspend fun upsert(server: Server): Long
}