package cz.nejakejtomas.bluemapminimap.client.server

import de.jensklingenberg.ktorfit.http.GET

interface ServerApiClient {
    @GET("settings.json")
    suspend fun settings(): Result<ServerSettings>
}