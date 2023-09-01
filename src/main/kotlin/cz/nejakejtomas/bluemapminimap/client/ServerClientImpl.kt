package cz.nejakejtomas.bluemapminimap.client

import cz.nejakejtomas.bluemapminimap.config.ServerConfig
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.cache.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class ServerClientImpl(private val serverConfig: ServerConfig) : ServerClient {
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

            val url = URLBuilder(
                serverConfig.getMapUrlOrDefault() ?: return@withContext null
            ).appendPathSegments("settings.json").build()
            val response: ServerSettings = client.get(url).body()
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