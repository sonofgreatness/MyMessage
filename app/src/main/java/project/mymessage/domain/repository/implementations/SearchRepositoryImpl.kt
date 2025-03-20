package project.mymessage.domain.repository.implementations

import project.mymessage.database.Daos.SearchDao
import project.mymessage.database.Entities.SearchQuery
import project.mymessage.domain.repository.interfaces.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(private val searchDao: SearchDao) : SearchRepository {
    override suspend fun addSearchQuery(entity: SearchQuery) = searchDao.addSearchQuery(entity)
    override suspend fun deleteSearchQuery(entity: String) = searchDao.deleteSearchQuery(entity)
    override suspend fun clearAllSearchQueries() = searchDao.clearAllSearchQueries()
    override suspend fun getAllSearchQueries(): List<SearchQuery> = searchDao.getAllSearchQueries()
    override suspend fun getParticularSearchQuery(query: String): List<SearchQuery>  = searchDao.getParticularSearchQuery(query)
}