package project.mymessage.database.Entities

import androidx.room.*
import project.mymessage.util.Enums
import java.sql.Timestamp

@Entity(
      tableName = "message",
      foreignKeys = [ForeignKey(
            entity = Conversation::class,
            parentColumns = ["from"],
            childColumns = ["from_id"],
            onDelete = ForeignKey.CASCADE
      )],
      indices = [Index(value = ["from_id"])]
)
data class Message(
      @PrimaryKey(autoGenerate = true) val id: Int = 0,
      @ColumnInfo(name = "from_id") val from_id: String,  // Foreign key
      @ColumnInfo(name = "to_id") val to_id: String?,
      @ColumnInfo(name = "content") val content: String,
      @ColumnInfo(name = "message_type") val messageType: Int, // e.g., SENT, RECEIVED
      @ColumnInfo(name = "date_created") val dateCreated: Timestamp = Timestamp(System.currentTimeMillis()),
      @ColumnInfo(name = "status") val status: Enums.MessageStatus = Enums.MessageStatus.SENT,
      @ColumnInfo(name = "is_read") val isRead: Boolean = false
)