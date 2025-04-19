package cz.nejakejtomas.bluemapminimap.config

import cz.nejakejtomas.bluemapminimap.dbs.repository.WorldRepository

class WorldConfig(private val wordRepository: WorldRepository, private val wordDefaults: WorldDefaults) {
    suspend fun getSavedMapName(): String? {
        return wordRepository.getMapName()
    }

    suspend fun getMapNameOrDefault(): String {
        return getSavedMapName() ?: wordDefaults.getMapName()
    }

    suspend fun setMapName(name: String?) {
        wordRepository.setMapName(name)
    }
}