package project.mymessage.database.Daos

import androidx.room.*
import project.mymessage.database.Entities.Conversation
import project.mymessage.database.Entities.ConversationWithMessages

@Dao
interface ConversationDao {
    @Insert
        (onConflict = OnConflictStrategy.IGNORE)
    suspend fun addConversation (conversation:Conversation)
    @Query("SELECT * FROM  conversation")
      suspend fun  getAllConversations() : List<Conversation>

    @Transaction
    @Query(
        "SELECT * FROM conversation"
    )
   suspend  fun getConversationsWithMessages(): List<ConversationWithMessages>

    @Transaction
    @Query(
        "SELECT * FROM conversation " +
                "INNER JOIN message ON conversation.`from` = message.from_id " +
                "WHERE conversation.`from` = :fromId"
    )
 suspend fun getConversationsWithMessagesFrom(fromId:String) : List<ConversationWithMessages>
    @Transaction
    @Query(
        "SELECT COUNT() FROM conversation " +
                "INNER JOIN message ON conversation.`from` = message.from_id " +
                "WHERE message.is_read = 0"
    )
  suspend fun  getTotalUnreadMessages() : Int


    @Transaction
    @Query(
        "SELECT * FROM conversation WHERE conversation.`from`  LIKE '%' || :search_term || '%'"
    )

  suspend  fun  getFilteredConversations(search_term :String) : List<ConversationWithMessages>


}