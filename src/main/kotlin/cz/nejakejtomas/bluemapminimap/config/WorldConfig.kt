package cz.nejakejtomas.bluemapminimap.config

import cz.nejakejtomas.bluemapminimap.dbs.WorldDao

class WorldConfig(private val wordDao: WorldDao, private val wordDefaults: WorldDefaults) {
    fun getMapName(): String? {
        return wordDao.getMapName()
    }

    suspend fun getMapNameOrDefault(): String {
        return getMapName() ?: wordDefaults.getMapName()
    }

    fun setMapName(name: String) {
        wordDao.setMapName(name)
    }
}