package cz.nejakejtomas.bluemapminimap.client.map

import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path
import java.awt.image.BufferedImage

interface MapApiClient {
    @GET("settings.json")
    suspend fun settings(): Result<MapSettings>

    @GET("tiles/1/x{x}/z{z}.png")
    suspend fun tileAt(@Path("x") x: Int, @Path("z") z: Int): Result<BufferedImage>
}