package project.mymessage.ui.viewModels

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import project.mymessage.R
import project.mymessage.database.Entities.Conversation
import project.mymessage.database.Entities.ConversationWithMessages
import project.mymessage.database.Entities.Message
import project.mymessage.domain.repository.interfaces.ConversationRepository
import project.mymessage.domain.repository.interfaces.MessageRepository
import project.mymessage.domain.use_cases.chat.SendMessageUseCase
import project.mymessage.domain.workers.IncomingSmsWorker
import project.mymessage.ui.contacts.Contact
import project.mymessage.util.operationalstates.SendMessageState
import javax.inject.Inject

@HiltViewModel
class ConversationViewModel @Inject constructor(
    private val  conversationRepository: ConversationRepository,
    private  val  messageRepository: MessageRepository,
    private val sendMessageUseCase: SendMessageUseCase,
    private val app : Application
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



    private val _currentSimDisplayName = MutableLiveData<String>()
    val currentSimDisplayName: LiveData<String> get() = _currentSimDisplayName

    private val _currentSimIcon = MutableLiveData<Drawable?>()
    val currentSimIcon: LiveData<Drawable?> get() = _currentSimIcon

    private val _currentSimIconTint = MutableLiveData<Int>()
    val currentSimIconTint: LiveData<Int> get() = _currentSimIconTint

    private val sharedPreferences: SharedPreferences =
        app.getSharedPreferences("sim_prefs", Context.MODE_PRIVATE)
    private val subscriptionManager: SubscriptionManager? =
        ContextCompat.getSystemService(app, SubscriptionManager::class.java)

    init {

        viewModelScope.launch(Dispatchers.IO) {
            loadSimData()
        }

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
    private suspend fun loadSimData() {
        val selectedSimSlot = sharedPreferences.getInt("selected_sim_slot", 0)
        updateSimData(selectedSimSlot)
    }


    private suspend fun updateSimData(simSlot: Int) {
        withContext(Dispatchers.Main){
            Log.d("SIM_Update","Sim Slot selected :$simSlot")
        }
        if (subscriptionManager == null) {
            withContext(Dispatchers.Main) {
                _currentSimDisplayName.value = "No SIM"
                _currentSimIcon.value = null
                _currentSimIconTint.value = android.graphics.Color.GRAY
            }

            return
        }

        try {
            val subscriptionInfo: SubscriptionInfo? =
                subscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(simSlot)
            if (subscriptionInfo != null) {
                withContext(Dispatchers.Main) {
                    _currentSimDisplayName.value =
                        subscriptionInfo.displayName?.toString() ?: "SIM $simSlot"

                    val iconBitmap: Bitmap = subscriptionInfo.createIconBitmap(app.applicationContext)
                    var simIconDrawable: Drawable = BitmapDrawable(app.resources, iconBitmap)

                    simIconDrawable = DrawableCompat.wrap(simIconDrawable.mutate())
                    DrawableCompat.setTint(simIconDrawable, subscriptionInfo.iconTint)
                    DrawableCompat.setTintMode(simIconDrawable, PorterDuff.Mode.SRC_IN)
                    _currentSimIcon.value = simIconDrawable
                    _currentSimIcon.value = BitmapDrawable(app.resources, iconBitmap)
                    _currentSimIconTint.value = subscriptionInfo.iconTint

                }
            } else {
                withContext(Dispatchers.Main) {
                    _currentSimDisplayName.value = "SIM $simSlot (Inactive)"
                    _currentSimIcon.value =
                        ContextCompat.getDrawable(app, R.drawable.baseline_sim_card_alert_24) // Replace with your placeholder icon
                    _currentSimIconTint.value = android.graphics.Color.GRAY
               }
            }
        } catch (e: SecurityException) {
            Log.e("SimManager", "SecurityException: ${e.message}")
            withContext(Dispatchers.Main) {
                _currentSimDisplayName.value = "Permission Denied"
                _currentSimIcon.value = null
                _currentSimIconTint.value = android.graphics.Color.RED
            }
        }

    }


    fun addConversation(entity : Conversation){}

    fun selectSimCard(simSlot: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            with(sharedPreferences.edit()) {
                putInt("selected_sim_slot", simSlot)
                apply()
            }
            updateSimData(simSlot)
        }
    }

    fun getCurrentSimSlot(): Int {
        return sharedPreferences.getInt("selected_sim_slot", 0)
    }

   fun updateFilteredMessages(search_term : String)
   {
       viewModelScope.launch {
           messageRepository.getFilteredMessages(search_term).let {
               _filteredMessages.value = it
           }
       }
   }

    fun sendMessageToContacts(contacts : List<Contact>,
                                message:String) {

    viewModelScope.launch {

        sendMessageUseCase(contacts[0], message, 1).collect {
            when(it)
            {
                is SendMessageState.Loading -> {
                    Toast.makeText(app.applicationContext, "Sending", Toast.LENGTH_SHORT).show()
                }
                is SendMessageState.Success ->{
                    Toast.makeText(app.applicationContext, "Sent ", Toast.LENGTH_SHORT).show()
                }
                is SendMessageState.Error ->{
                    Toast.makeText(app.applicationContext, "Error ${it.exceptionMessage} ", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    }
    fun updateConversations(){
        viewModelScope.launch {
            conversationRepository.getTotalUnreadMessages().let{
                _totalUnreadMessages.value = it
            }

            conversationRepository.getAllConversations().let{
                _readAllData.value = it
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

    fun deleteMessagesFromConversation(to_id:String){
        viewModelScope.launch {
            messageRepository.deleteMessagesFromConversation(to_id)
        }
    }

}