package project.mymessage.domain.repository.implementations

import project.mymessage.database.Daos.ConversationDao
import project.mymessage.database.Entities.Conversation
import project.mymessage.database.Entities.ConversationWithMessages
import project.mymessage.domain.repository.interfaces.ConversationRepository
import javax.inject.Inject

class ConversationRepositoryImpl @Inject constructor(private val conversationDao: ConversationDao): ConversationRepository {


    override   suspend fun addConversation(entity: Conversation)  = conversationDao.addConversation(entity)
    override   suspend fun getAllConversations()  = conversationDao.getAllConversations()
    override suspend fun getConversationsWithMessages() = conversationDao.getConversationsWithMessages()
    override  suspend fun getConversationsWithMessagesFrom(fromId: String)
                               : List<ConversationWithMessages> =conversationDao.getConversationsWithMessagesFrom(fromId)
    override suspend fun getTotalUnreadMessages() = conversationDao.getTotalUnreadMessages()

    override   suspend  fun  getFilteredConversations(search_term :String) :
            List<ConversationWithMessages> = conversationDao.getFilteredConversations(search_term)

    override  suspend fun updateConversation(entity: Conversation) {
        deleteConversationFrom(entity.from)
        return  conversationDao.addConversation(entity)
    }

    override  suspend fun  getConversation(from :String, to :String) : Conversation? = conversationDao.getConversation(from,to)
    override  suspend fun  deleteConversationFrom(from:String) = conversationDao.deleteConversationFrom(from)



}