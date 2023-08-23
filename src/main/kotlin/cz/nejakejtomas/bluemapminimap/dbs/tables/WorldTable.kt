package cz.nejakejtomas.bluemapminimap.dbs.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object WorldTable : IntIdTable("world") {
    val dimension = text("dimension")
    val mapName = text("mapName").nullable()
    val server = reference("server", ServerTable, ReferenceOption.NO_ACTION)

    init {
        index(true, dimension, server)
    }
}