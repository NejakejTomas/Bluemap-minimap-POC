package cz.nejakejtomas.bluemapminimap.client

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.cache.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import javax.imageio.ImageIO
import kotlin.math.abs

class MapClientImpl(private val world: String) : MapClient {
    // TODO - inject?
    private val path = "http://nejakejtomas.cz/maps"
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
        install(HttpCache)
    }

    private var savedSettings: MapSettings? = null
    private suspend fun settings(): MapSettings? = withContext(Dispatchers.IO) {
        try {
            savedSettings?.let { return@withContext it }

            val response: MapSettings = client.get("${path}/${world}/settings.json").body()
            savedSettings = response

            response

        } catch (e: Exception) {
            null
        }
    }

    private fun getPathFromCoordinate(coordinate: Int): String {
        if (coordinate / 10 == 0) return coordinate.toString()

        return getPathFromCoordinate(coordinate / 10) + abs(coordinate % 10).toString()
    }

    private fun getPathFromCoordinates(x: Int, z: Int): String {
        return "${path}/${world}/tiles/1/x${getPathFromCoordinate(x)}/z${getPathFromCoordinate(z)}.png"
    }

    override suspend fun tileWidth(): Int? = withContext(Dispatchers.IO) {
        // Also for my sanity
        assert(settings()?.tilesSettings?.width == settings()?.tilesSettings?.height)
        return@withContext settings()?.tilesSettings?.width
    }

    override suspend fun tileHeight(): Int? = withContext(Dispatchers.IO) {
        assert(settings()?.tilesSettings?.width == settings()?.tilesSettings?.height)
        return@withContext settings()?.tilesSettings?.height
    }

    override suspend fun tileAt(x: Int, z: Int): BufferedImage? = withContext(Dispatchers.IO) {
        try {
            val request = client.get(Url(getPathFromCoordinates(x, z)))

            if (request.status != HttpStatusCode.OK) null
            else {
                val byteArray = request.readBytes()

                ByteArrayInputStream(byteArray).use {
                    ImageIO.read(it)
                }
            }

        } catch (e: Exception) {
            null
        }
    }
}