package cz.nejakejtomas.bluemapminimap.client

import cz.nejakejtomas.bluemapminimap.common.Size
import java.awt.image.BufferedImage

interface MapClient {
    suspend fun tileWidth(): Int?
    suspend fun tileHeight(): Int?
    suspend fun tileSize(): Size<Int>?
    suspend fun tileAt(x: Int, z: Int): BufferedImage?
}