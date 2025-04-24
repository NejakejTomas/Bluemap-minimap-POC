package cz.nejakejtomas.bluemapminimap.client.server

fun interface ServerApiFactory {
    suspend operator fun invoke(mapUrl: String): ServerApiClient
}