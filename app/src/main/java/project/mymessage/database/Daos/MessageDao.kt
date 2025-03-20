package project.mymessage.database.Daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import project.mymessage.database.Entities.Message
@Dao
interface MessageDao  {

    @Insert
        (onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMessage (message: Message)
    @Query("SELECT * FROM message WHERE content LIKE '%' || :search_term || '%'")
    suspend fun getFilteredMessages(search_term: String): List<Message>
}