package cz.nejakejtomas.bluemapminimap.dbs.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import cz.nejakejtomas.bluemapminimap.dbs.entity.MinimapSettings
import kotlinx.coroutines.flow.Flow

@Dao
interface MinimapSettingsDao {
    @Query("SELECT * FROM minimap_settings WHERE id = 42 LIMIT 1")
    fun select(): Flow<MinimapSettings?>

    @Query("SELECT * FROM minimap_settings WHERE id = 42 LIMIT 1")
    suspend fun selectSuspend(): MinimapSettings?

    @Upsert
    suspend fun upsert(settings: MinimapSettings): Long
}