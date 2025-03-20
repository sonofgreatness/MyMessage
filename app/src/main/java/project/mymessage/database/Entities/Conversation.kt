package project.mymessage.database.Entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp
import java.util.*
@Entity(tableName = "conversation")
data class Conversation(
    @PrimaryKey
    @ColumnInfo(name = "from") val from: String,
    @ColumnInfo(name = "to") val to: String,
    @ColumnInfo(name = "DateCreated")
    val dateCreated: Timestamp,

    )
