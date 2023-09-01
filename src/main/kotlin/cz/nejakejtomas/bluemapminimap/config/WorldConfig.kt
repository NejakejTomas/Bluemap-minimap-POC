package cz.nejakejtomas.bluemapminimap.config

import cz.nejakejtomas.bluemapminimap.dbs.WorldDao

class WorldConfig(private val wordDao: WorldDao, private val wordDefaults: WorldDefaults) {
    fun getSavedMapName(): String? {
        return wordDao.getMapName()
    }

    suspend fun getMapNameOrDefault(): String {
        return getSavedMapName() ?: wordDefaults.getMapName()
    }

    fun setMapName(name: String?) {
        wordDao.setMapName(name)
    }
}