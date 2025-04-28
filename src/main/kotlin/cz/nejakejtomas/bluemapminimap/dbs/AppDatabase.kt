package cz.nejakejtomas.bluemapminimap.dbs

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cz.nejakejtomas.bluemapminimap.dbs.converters.SizeIntTypeConverter
import cz.nejakejtomas.bluemapminimap.dbs.dao.MinimapSettingsDao
import cz.nejakejtomas.bluemapminimap.dbs.dao.ServerDao
import cz.nejakejtomas.bluemapminimap.dbs.dao.WorldDao
import cz.nejakejtomas.bluemapminimap.dbs.entity.MinimapSettings
import cz.nejakejtomas.bluemapminimap.dbs.entity.Server
import cz.nejakejtomas.bluemapminimap.dbs.entity.World

@Database(entities = [Server::class, World::class, MinimapSettings::class], version = 1)
@TypeConverters(SizeIntTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun serverDao(): ServerDao
    abstract fun worldDao(): WorldDao
    abstract fun minimapSettingsDao(): MinimapSettingsDao
}