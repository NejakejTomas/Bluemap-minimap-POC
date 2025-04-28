package cz.nejakejtomas.bluemapminimap.dbs.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "server", indices = [Index(value = ["url"], unique = true)])
data class Server(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val url: String,
    val mapUrl: String?,
)