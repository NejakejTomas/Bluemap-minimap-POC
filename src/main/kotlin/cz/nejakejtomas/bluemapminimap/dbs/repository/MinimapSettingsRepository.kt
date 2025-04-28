package cz.nejakejtomas.bluemapminimap.dbs.repository

import androidx.room.deferredTransaction
import androidx.room.immediateTransaction
import androidx.room.useWriterConnection
import cz.nejakejtomas.bluemapminimap.dbs.AppDatabase
import cz.nejakejtomas.bluemapminimap.dbs.dao.MinimapSettingsDao
import cz.nejakejtomas.bluemapminimap.dbs.entity.MinimapSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow

class MinimapSettingsRepository(
    private val database: AppDatabase,
    private val minimapSettingsDao: MinimapSettingsDao
) {
    suspend fun update(block: (MinimapSettings) -> MinimapSettings) {
        database.useWriterConnection { transactor ->
            transactor.immediateTransaction {
                val current = minimapSettingsDao.selectSuspend() ?: MinimapSettings()
                val new = block(current)
                minimapSettingsDao.upsert(new)
            }
        }
    }

    suspend fun selectOne(): MinimapSettings {
        return database.useWriterConnection { transactor ->
            transactor.deferredTransaction {
                val current = minimapSettingsDao.selectSuspend()

                if (current == null) {
                    val new = MinimapSettings()
                    minimapSettingsDao.upsert(new)
                    new
                } else current
            }
        }
    }

    fun select(): Flow<MinimapSettings> = flow {
        database.useWriterConnection { transactor ->
            transactor.deferredTransaction {
                val current = minimapSettingsDao.selectSuspend()
                if (current == null) minimapSettingsDao.upsert(MinimapSettings())
            }
        }

        emitAll(minimapSettingsDao.select().filterNotNull())
    }
}