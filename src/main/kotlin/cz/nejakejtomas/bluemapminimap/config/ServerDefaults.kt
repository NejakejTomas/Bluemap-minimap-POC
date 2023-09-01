package cz.nejakejtomas.bluemapminimap.config

import cz.nejakejtomas.bluemapminimap.Server
import cz.nejakejtomas.bluemapminimap.urlFromMinecraft
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.cache.*
import io.ktor.client.request.*
import io.ktor.http.*

class ServerDefaults(private val server: Server) {
    private val client = HttpClient(CIO) {
        install(HttpCache)
        install(HttpTimeout) {
            requestTimeoutMillis = TIMEOUT
        }
    }

    private var validUrl: Url? = null
    suspend fun getMapUrl(): Url? {
        val current = validUrl
        if (current != null) return current

        try {
            transformations.forEach {
                val url = tryUrl(it(urlFromMinecraft(server.url) ?: return null))

                if (url != null) {
                    validUrl = url
                    return url
                }
            }
        } catch (_: Exception) {
        }

        return null
    }

    private suspend fun tryUrl(url: Url): Url? {
        return try {
            // Sadly Bluemap returns HTTP 400 bad request when HEAD is used, so we have to use GET
            if (client.get(url).status.value in (200 until 300)) url
            else null
        } catch (_: Exception) {
            null
        }
    }

    companion object {
        private const val TIMEOUT: Long = 1000
        private val transformations: List<(Url) -> Url> = listOf({
            URLBuilder(it).apply {
                port = 80
                protocol = URLProtocol.HTTP
            }.build()
        }, {
            URLBuilder(it).apply {
                port = 443
                protocol = URLProtocol.HTTPS
            }.build()
        }, {
            // Default Bluemap URL
            URLBuilder(it).apply {
                port = 8100
                protocol = URLProtocol.HTTP
            }.build()
        })
    }
}