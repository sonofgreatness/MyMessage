package project.mymessage.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import project.mymessage.database.Entities.SearchQuery
import project.mymessage.domain.repository.interfaces.ConversationRepository
import project.mymessage.domain.repository.interfaces.SearchRepository
import javax.inject.Inject

@HiltViewModel
class SearchViewModel  @Inject constructor(private val searchRepository: SearchRepository,
                                           private  val conversationRepository: ConversationRepository,
                                           ): ViewModel() {


    private val _searchQueries = MutableLiveData<List<SearchQuery>>()
    val searchQueries :LiveData<List<SearchQuery>> get() = _searchQueries




    init {


        viewModelScope.launch {
            searchRepository.getAllSearchQueries().let{
                _searchQueries.value = it
            }

        }
    }

    fun addSearchQuery(query : String)
    {
        val entity = SearchQuery(term = query)
        viewModelScope.launch {
            searchRepository.addSearchQuery(entity)
        }
    }
    fun deleteSearchQuery (query: String)
    {
        viewModelScope.launch {
            searchRepository.deleteSearchQuery(query)
            searchRepository.getAllSearchQueries().let{
                _searchQueries.value = it
            }
        }
    }

    fun clearAllSearchQueries(){
        viewModelScope.launch {
            searchRepository.clearAllSearchQueries()

            searchRepository.getAllSearchQueries().let{
                _searchQueries.value = it
            }
        }
    }


    fun updateQueries()
    {
        viewModelScope.launch {
            searchRepository.getAllSearchQueries().let{
                _searchQueries.value = it
            }

        }
    }




}