package project.mymessage.domain.repository.interfaces

import project.mymessage.database.Entities.Conversation
import project.mymessage.database.Entities.ConversationWithMessages

interface ConversationRepository {

    suspend fun addConversation(entity :Conversation)
    suspend fun getAllConversations(): List<Conversation>
     suspend fun getConversationsWithMessages(): List<ConversationWithMessages>
    suspend fun getConversationsWithMessagesFrom(fromId:String) : List<ConversationWithMessages>
    suspend fun  getTotalUnreadMessages() : Int
    suspend  fun  getFilteredConversations(search_term :String) : List<ConversationWithMessages>
}