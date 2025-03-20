package project.mymessage.domain.repository.interfaces

import project.mymessage.database.Entities.SearchQuery

interface SearchRepository {


    suspend fun addSearchQuery(entity: SearchQuery)
    suspend fun  deleteSearchQuery(query: String)
    suspend fun clearAllSearchQueries()
    suspend fun  getAllSearchQueries() :List<SearchQuery>
    suspend fun  getParticularSearchQuery(query :String) : List<SearchQuery>

}