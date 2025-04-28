package cz.nejakejtomas.bluemapminimap.dbs.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import cz.nejakejtomas.bluemapminimap.common.Size

@Entity(tableName = "minimap_settings")
data class MinimapSettings(
    @PrimaryKey(autoGenerate = false)
    val id: Long = 42,
    val doRotate: Boolean = false,
    val targetBlockSize: Size<Int> = Size(750, 750),
    val debugRender: Boolean = false,
)