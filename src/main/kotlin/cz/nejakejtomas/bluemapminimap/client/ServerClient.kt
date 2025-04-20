package cz.nejakejtomas.bluemapminimap.client

import cz.nejakejtomas.bluemapminimap.model.ServerId

interface ServerClient {
    suspend fun maps(serverId: ServerId): List<String>?
}