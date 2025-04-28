package cz.nejakejtomas.bluemapminimap.dbs.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "world",
    indices = [Index(value = ["serverId"]), Index(value = ["dimension", "serverId"], unique = true)],
    foreignKeys = [ForeignKey(
        entity = Server::class,
        parentColumns = ["id"],
        childColumns = ["serverId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class World(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val serverId: Long,
    val dimension: String,
    val mapName: String?,
)
