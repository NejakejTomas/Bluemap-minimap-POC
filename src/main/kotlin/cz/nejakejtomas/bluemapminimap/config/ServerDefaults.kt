package cz.nejakejtomas.bluemapminimap.config

import cz.nejakejtomas.bluemapminimap.Server

class ServerDefaults(private val server: Server) {
    fun getMapUrl(): String {
        return server.url
        TODO("Not yet implemented")
    }
}