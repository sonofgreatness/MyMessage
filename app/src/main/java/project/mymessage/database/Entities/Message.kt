package project.mymessage.database.Entities

import androidx.room.*
import project.mymessage.util.Enums
import java.sql.Timestamp
import java.util.UUID

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
      @PrimaryKey val id: UUID,
      @ColumnInfo(name = "from_id") val from_id: String,
      @ColumnInfo(name = "to_id") val to_id: String?,
      @ColumnInfo(name = "content") val content: String,
      @ColumnInfo(name = "message_type") val messageType: Int,
      @ColumnInfo(name = "date_created") val dateCreated: Timestamp = Timestamp(System.currentTimeMillis()),
      @ColumnInfo(name = "status") var status: Enums.MessageStatus = Enums.MessageStatus.SENT,
      @ColumnInfo(name = "is_read") val isRead: Boolean = false
)