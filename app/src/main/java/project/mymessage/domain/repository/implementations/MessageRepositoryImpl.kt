package project.mymessage.domain.repository.implementations

import project.mymessage.database.Daos.MessageDao
import project.mymessage.database.Entities.Message
import project.mymessage.domain.repository.interfaces.MessageRepository
import java.util.UUID
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(private val messageDao: MessageDao): MessageRepository {
    override suspend fun addMessage(entity: Message) = messageDao.addMessage(entity)
    override  suspend fun getFilteredMessages(search_term: String):
            List<Message> = messageDao.getFilteredMessages(search_term)

    override suspend fun getMessageById(id: UUID) = messageDao.getMessageById(id)
    override suspend fun updateMessage(message: Message) = messageDao.updateMessage(message)
    override suspend fun deleteMessageById(id: UUID) = messageDao.deleteMessageById(id)
    override suspend fun deleteMessagesFromConversation(to_id:String) = messageDao.deleteMessagesFromConversation(to_id)

}