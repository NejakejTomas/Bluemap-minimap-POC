package cz.nejakejtomas.bluemapminimap.dbs.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object ServerTable : IntIdTable("server") {
    val serverUrl = text("serverUrl").index(isUnique = true)
    val mapUrl = text("mapUrl").nullable()
}