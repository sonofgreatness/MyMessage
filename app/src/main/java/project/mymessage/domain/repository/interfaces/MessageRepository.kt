package project.mymessage.domain.repository.interfaces

import project.mymessage.database.Entities.Message

interface MessageRepository {
    suspend fun addMessage (entity: Message)
    suspend fun getFilteredMessages(search_term: String): List<Message>
}