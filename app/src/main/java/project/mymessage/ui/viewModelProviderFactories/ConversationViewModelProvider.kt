package project.mymessage.ui.viewModelProviderFactories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import project.mymessage.domain.repository.interfaces.ConversationRepository
import project.mymessage.domain.repository.interfaces.MessageRepository
import project.mymessage.ui.viewModels.ConversationViewModel
import javax.inject.Inject

class ConversationViewModelProvider
@Inject constructor (
    private val conversationRepository: ConversationRepository,
      private val messageRepository: MessageRepository):ViewModelProvider.Factory

{

    override fun <T : ViewModel> create(modelClass: Class<T>): T{
        return ConversationViewModel(conversationRepository,
        messageRepository)
        as T
    }

}


