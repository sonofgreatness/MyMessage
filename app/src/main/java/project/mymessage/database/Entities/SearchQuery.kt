package project.mymessage.database.Entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp
import java.util.*

@Entity(tableName = "search_history")
data class SearchQuery (
    @PrimaryKey
    @ColumnInfo(name = "term")
    val term : String,
    @ColumnInfo(name = "date_created")
    val dateCreated: Timestamp = Timestamp(System.currentTimeMillis())

)