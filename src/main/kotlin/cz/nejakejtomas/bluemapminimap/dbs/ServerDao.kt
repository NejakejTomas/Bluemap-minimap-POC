package cz.nejakejtomas.bluemapminimap.dbs

import cz.nejakejtomas.bluemapminimap.Server
import cz.nejakejtomas.bluemapminimap.dbs.tables.ServerTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update

@Suppress("RemoveRedundantQualifierName")
class ServerDao(private val server: Server, private val database: Database) {
    fun getMapUrl(): String? = database.transaction {
        val result = ServerTable.select { ServerTable.serverUrl eq server.url }
            .firstOrNull() ?: return@transaction null

        return@transaction result[ServerTable.mapUrl]
    }

    fun setMapUrl(mapUrl: String?) = database.transaction {
        val result = ServerTable.select { ServerTable.serverUrl eq server.url }
            .firstOrNull()

        if (result == null) ServerTable.insert {
            it[ServerTable.serverUrl] = server.url
            it[ServerTable.mapUrl] = mapUrl
        }
        else ServerTable.update({ ServerTable.id eq result[ServerTable.id] }) {
            it[ServerTable.mapUrl] = mapUrl
        }
    }

    fun getId(): Int = database.transaction {
        // Sadly upsert is broken
        val result = ServerTable.select { ServerTable.serverUrl eq server.url }
            .firstOrNull()

        if (result != null) return@transaction result[ServerTable.id].value

        ServerTable.insertAndGetId {
            it[ServerTable.serverUrl] = server.url
        }.value
    }
}