package cz.nejakejtomas.bluemapminimap.render

import com.mojang.blaze3d.vertex.PoseStack

interface TileMap {
    fun render(pose: PoseStack, positionX: Double, positionZ: Double, rotation: Float?)
}