package project.mymessage.domain.repository.interfaces

import androidx.room.Query
import project.mymessage.database.Entities.Message
import java.util.UUID

interface MessageRepository {
    suspend fun addMessage (entity: Message)
    suspend fun getFilteredMessages(search_term: String): List<Message>
    suspend fun updateMessage(message: Message)
    suspend fun  getMessageById(id : UUID) : Message
    suspend fun deleteMessageById(id: UUID)
    suspend fun deleteMessagesFromConversation(to_id:String)

}