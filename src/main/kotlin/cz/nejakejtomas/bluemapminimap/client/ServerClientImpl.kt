package cz.nejakejtomas.bluemapminimap.client

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.cache.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class ServerClientImpl : ServerClient {
    // TODO - inject?
    private val path = "http://nejakejtomas.cz"
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
        install(HttpCache)
    }

    private var savedSettings: ServerSettings? = null
    private suspend fun settings(): ServerSettings? = withContext(Dispatchers.IO) {
        try {
            savedSettings?.let { return@withContext it }

            val response: ServerSettings = client.get("${path}/settings.json").body()
            savedSettings = response

            response

        } catch (e: Exception) {
            null
        }
    }

    override suspend fun maps(): List<String>? = withContext(Dispatchers.IO) {
        return@withContext settings()?.maps
    }

}