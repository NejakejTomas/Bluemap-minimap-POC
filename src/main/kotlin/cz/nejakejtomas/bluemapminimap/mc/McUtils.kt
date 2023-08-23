package cz.nejakejtomas.bluemapminimap.mc

import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientLevel

val Minecraft.currentWorldName: String
    get() = this.level!!.currentWorldName

val ClientLevel.currentWorldName: String
    get() = this.dimension().location().toString()