package project.mymessage.database.Entities

import androidx.room.Embedded
import androidx.room.Relation

data class ConversationWithMessages(
    @Embedded val conversation: Conversation,
    @Relation(
        parentColumn = "from",
        entityColumn = "from_id"
    )
    val messages: List<Message>
)