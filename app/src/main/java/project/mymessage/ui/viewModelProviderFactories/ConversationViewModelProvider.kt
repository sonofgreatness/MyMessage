package project.mymessage.ui.viewModelProviderFactories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import project.mymessage.domain.repository.interfaces.ConversationRepository
import project.mymessage.domain.repository.interfaces.MessageRepository
import project.mymessage.domain.use_cases.chat.SendMessageUseCase
import project.mymessage.ui.viewModels.ConversationViewModel
import javax.inject.Inject

class ConversationViewModelProvider
@Inject constructor (
    private val conversationRepository: ConversationRepository,
      private val messageRepository: MessageRepository,
    private val sendMessageUseCase: SendMessageUseCase,
    private val app : Application
):ViewModelProvider.Factory

{

    override fun <T : ViewModel> create(modelClass: Class<T>): T{
        return ConversationViewModel(conversationRepository,
        messageRepository, sendMessageUseCase, app)
        as T
    }

}


