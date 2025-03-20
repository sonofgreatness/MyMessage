package project.mymessage.ui.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import project.mymessage.database.Entities.Conversation
import project.mymessage.database.Entities.ConversationWithMessages
import project.mymessage.database.Entities.Message
import project.mymessage.domain.repository.interfaces.ConversationRepository
import project.mymessage.domain.repository.interfaces.MessageRepository
import project.mymessage.util.Enums
import java.sql.Timestamp
import javax.inject.Inject

@HiltViewModel
class ConversationViewModel @Inject constructor(
  private val  conversationRepository: ConversationRepository,
 private  val  messageRepository: MessageRepository
): ViewModel(){


        private val _usableState = MutableLiveData<String>()
        val usableState: LiveData<String>
            get() = _usableState



        private val _readAllData = MutableLiveData<List<Conversation>>()
        val readAllData: LiveData<List<Conversation>>
            get() = _readAllData


    private val _readAllConversations = MutableLiveData<List<ConversationWithMessages>>()
    val readAllConversations: LiveData<List<ConversationWithMessages>>
        get() = _readAllConversations

private val _filteredConversations = MutableLiveData<List<ConversationWithMessages>>()
    val filteredConversations : LiveData<List<ConversationWithMessages>>
      get() = _filteredConversations

    private val _currentConversation = MutableLiveData<List<ConversationWithMessages>>()
    val currentConversation: LiveData<List<ConversationWithMessages>>
        get() = _currentConversation

    private val  _totalUnreadMessages = MutableLiveData<Int>()
        val  totalUnreadMessages:LiveData<Int> get() = _totalUnreadMessages

    private  val _filteredMessages = MutableLiveData<List<Message>>()
         val filteredMessages:LiveData<List<Message>> get() = _filteredMessages

    init {



       /* viewModelScope.launch {

            val entity = Conversation(
                from = "Simphiwe",
            to="MTN",
            dateCreated =  Timestamp(System.currentTimeMillis()))
            val entity2 = Conversation(
                from = "Senzo",
                to="Eswatini Mobile",
                dateCreated =  Timestamp(System.currentTimeMillis()))
            conversationRepository.addConversation(entity)
            conversationRepository.addConversation(entity2)
            val entity3 = Message(
                from_id = "Simphiwe",
                to_id = "MTN",
                content =  "Hey , you owe me an apology",
                messageType =  Enums.MessageType.Outgoing.value)
            val entity4 = Message(
                from_id = "Senzo",
                to_id = "Eswatini Mobile",
                content =  "I love my life.",
                messageType =  Enums.MessageType.Outgoing.value)
            messageRepository.addMessage(entity3)
            messageRepository.addMessage(entity4)



        }*/


        viewModelScope.launch {
            val conversations = conversationRepository.getConversationsWithMessages()
            withContext(Dispatchers.Main) {
                _readAllConversations.value = conversations
            }

        }

        viewModelScope.launch {
            conversationRepository.getAllConversations().let{
                _readAllData.postValue(it)
            }
        }

        viewModelScope.launch{
            conversationRepository.getTotalUnreadMessages().let{
                _totalUnreadMessages.value = it
            }
        }


    }


    fun addConversation(entity : Conversation){}



   fun updateFilteredMessages(search_term : String)
   {
       viewModelScope.launch {
           messageRepository.getFilteredMessages(search_term).let {
               _filteredMessages.value = it
           }
       }
   }


    fun updateConversations(){
        viewModelScope.launch {
            conversationRepository.getTotalUnreadMessages().let{
                _totalUnreadMessages.value = it
            }

            conversationRepository.getAllConversations().let{
                _readAllData.postValue(it)
            }


        }
    }


    fun updateFilteredConversations(search_term: String){
        viewModelScope.launch {
            conversationRepository.getFilteredConversations(search_term).let {
                _filteredConversations.value = it
            }
        }
    }

    fun  getConversationsWithMessagesFrom(fromId:String)
     {
      viewModelScope.launch {
      conversationRepository.getConversationsWithMessagesFrom(fromId).let{
          _currentConversation.value = it
      }
      }
    }

}