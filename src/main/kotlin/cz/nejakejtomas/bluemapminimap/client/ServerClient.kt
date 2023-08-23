package cz.nejakejtomas.bluemapminimap.client

interface ServerClient {
    suspend fun maps(): List<String>?
}