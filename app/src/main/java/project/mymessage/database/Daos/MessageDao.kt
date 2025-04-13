package project.mymessage.database.Daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import project.mymessage.database.Entities.Message
import java.util.UUID

@Dao
interface MessageDao  {

    @Insert
        (onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMessage (message: Message)
    @Query("SELECT * FROM message WHERE content LIKE '%' || :search_term || '%'")
    suspend fun getFilteredMessages(search_term: String): List<Message>

     @Update
     suspend fun updateMessage(message: Message)
     @Query
         ("SELECT * FROM message WHERE id=:id")
     suspend fun  getMessageById(id :UUID) : Message

}