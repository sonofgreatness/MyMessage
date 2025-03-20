package project.mymessage.database.Daos

import androidx.room.*
import project.mymessage.database.Entities.SearchQuery

@Dao
interface SearchDao {
    @Insert
        (onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSearchQuery(entity: SearchQuery)
    @Query("DELETE  FROM search_history WHERE term =:query")
    suspend fun  deleteSearchQuery(query: String)
    @Query("DELETE FROM search_history")
    suspend fun clearAllSearchQueries()

    @Query("SELECT * FROM search_history")
    suspend fun  getAllSearchQueries() :List<SearchQuery>

    @Query("SELECT * FROM search_history WHERE term =:query")
    suspend fun  getParticularSearchQuery(query :String) : List<SearchQuery>

}