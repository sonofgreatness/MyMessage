package project.mymessage.ui.viewModelProviderFactories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import project.mymessage.domain.repository.interfaces.ConversationRepository
import project.mymessage.domain.repository.interfaces.SearchRepository
import project.mymessage.ui.viewModels.SearchViewModel
import javax.inject.Inject

class SearchViewModelProvider@Inject constructor(private val searchRepository: SearchRepository,
                                                 private  val conversationRepository: ConversationRepository,

): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T{
        return SearchViewModel(searchRepository,conversationRepository,
           )
                as T
}

}