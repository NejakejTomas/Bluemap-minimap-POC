package cz.nejakejtomas.bluemapminimap.dbs

import cz.nejakejtomas.bluemapminimap.World
import cz.nejakejtomas.bluemapminimap.dbs.tables.WorldTable
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update

@Suppress("RemoveRedundantQualifierName")
class WorldDao(private val world: World, private val database: Database, private val serverDao: ServerDao) {
    fun getMapName(): String? = database.transaction {
        val id = serverDao.getId()

        val result = WorldTable.select { (WorldTable.serverUrl eq id) and (WorldTable.dimension eq world.dimension) }
            .firstOrNull() ?: return@transaction null

        return@transaction result[WorldTable.mapName]
    }

    fun setMapName(mapName: String?) = database.transaction {
        val id = serverDao.getId()

        val result = WorldTable.select { (WorldTable.serverUrl eq id) and (WorldTable.dimension eq world.dimension) }
            .firstOrNull()

        if (result == null) WorldTable.insert {
            it[WorldTable.dimension] = world.dimension
            it[WorldTable.mapName] = mapName
            it[WorldTable.serverUrl] = id
        }
        else WorldTable.update({ WorldTable.id eq result[WorldTable.id] }) {
            it[WorldTable.mapName] = mapName
        }
    }
}