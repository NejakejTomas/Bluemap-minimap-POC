package cz.nejakejtomas.bluemapminimap.client

import java.awt.image.BufferedImage

interface MapClient {
    suspend fun tileWidth(): Int?
    suspend fun tileHeight(): Int?
    suspend fun tileAt(x: Int, z: Int): BufferedImage?
}