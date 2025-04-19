package cz.nejakejtomas.bluemapminimap.dbs

import androidx.room.Database
import androidx.room.RoomDatabase
import cz.nejakejtomas.bluemapminimap.dbs.dao.ServerDao
import cz.nejakejtomas.bluemapminimap.dbs.dao.WorldDao
import cz.nejakejtomas.bluemapminimap.dbs.entity.Server
import cz.nejakejtomas.bluemapminimap.dbs.entity.World

@Database(entities = [Server::class, World::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun serverDao(): ServerDao
    abstract fun worldDao(): WorldDao
}