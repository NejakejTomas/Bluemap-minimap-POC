package cz.nejakejtomas.bluemapminimap.usecase.mapurl

import cz.nejakejtomas.bluemapminimap.ensureHttp
import cz.nejakejtomas.bluemapminimap.model.MapId
import cz.nejakejtomas.bluemapminimap.model.ServerId
import cz.nejakejtomas.bluemapminimap.runSuspendCatching
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

class GuessMapUrlUseCase(
    private val httpClient: HttpClient,
) {
    suspend operator fun invoke(serverId: ServerId): MapId? = withContext(Dispatchers.IO) {
        val suspectUrl = runCatching { Url(ensureHttp(serverId.url)) }.getOrNull() ?: return@withContext null

        transformations.map { transformation ->
            val transformed = transformation(suspectUrl)

            async {
                // Sadly Bluemap returns HTTP 400 bad request when HEAD is used, so we have to use GET
                runSuspendCatching {
                    val response = httpClient.get(transformed)
                    if (response.status.isSuccess()) MapId(transformed.toString())
                    else null
                }.getOrNull()
            }
        }.awaitAll().firstOrNull { it != null }
    }

    companion object {
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