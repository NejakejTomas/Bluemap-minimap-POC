package cz.nejakejtomas.bluemapminimap.client

import cz.nejakejtomas.bluemapminimap.client.converters.BufferedImageConverterFactory
import cz.nejakejtomas.bluemapminimap.client.converters.ResultConverterFactory
import cz.nejakejtomas.bluemapminimap.client.map.MapApiFactory
import cz.nejakejtomas.bluemapminimap.client.map.createMapApiClient
import cz.nejakejtomas.bluemapminimap.client.server.ServerApiFactory
import cz.nejakejtomas.bluemapminimap.client.server.createServerApiClient
import cz.nejakejtomas.bluemapminimap.ensureEndsWith
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.*
import io.ktor.http.*
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


val networkModule = module {
    singleOf(Client::client)
    single {
        Ktorfit.Builder()
            .httpClient(get<HttpClient>())
            .converterFactories(ResultConverterFactory, BufferedImageConverterFactory)
    }

    single {
        ServerApiFactory { mapUrl ->
            val ktorfit = get<Ktorfit.Builder>().baseUrl(mapUrl.ensureEndsWith("/")).build()
            ktorfit.createServerApiClient()
        }
    }

    single {
        MapApiFactory { mapId, mapRoot, mapDimensionId ->
            val url = URLBuilder(mapId.mapUrl).apply {
                path(mapRoot, mapDimensionId.mapName)
                build()
            }.toString().ensureEndsWith("/")

            val ktorfit = get<Ktorfit.Builder>().baseUrl(url, false).build()
            ktorfit.createMapApiClient()
        }
    }
}