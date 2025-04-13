package project.mymessage.domain.use_cases.chat


import android.app.Application
import android.content.Context
import android.telephony.SmsManager
import android.telephony.SubscriptionManager
import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import project.mymessage.database.Entities.Conversation
import project.mymessage.database.Entities.Message
import project.mymessage.domain.repository.interfaces.ConversationRepository
import project.mymessage.domain.repository.interfaces.MessageRepository
import project.mymessage.ui.contacts.Contact
import project.mymessage.util.Constants.Companion.PLACEHOLDER_CONVERSATION
import project.mymessage.util.Enums
import project.mymessage.util.operationalstates.SendMessageState
import java.sql.Timestamp
import java.util.UUID
import javax.inject.Inject

class SendMessageUseCase
@Inject constructor (private val messageRepository: MessageRepository,
val app: Application,
    private val conversationRepository: ConversationRepository
)
{

    /*
     *1 Create a message object with status = DRAFT
     *2 send  message GSM
     *     update   status of message
     *
    ********************************************/
    operator  fun invoke(contact : Contact, message: String, simCard : Int? = null ):
            Flow<SendMessageState> = flow{
                val msgId = UUID.randomUUID()
                val entity  = Message(
                    id = msgId,
                    from_id =  PLACEHOLDER_CONVERSATION,
                    to_id = contact.phoneNumbers[0],
                    content =  message,
                    messageType =  Enums.MessageType.Outgoing.value,
                    status = Enums.MessageStatus.DRAFT)


            generatePlaceholderConversation(contact)
            messageRepository.addMessage(entity)
            emit(SendMessageState.Loading)
            val sendingNumber = simCard.toString().trim()
         generateNewSenderConversation(sendingNumber, contact.phoneNumbers[0])

        try{

            val smsManager : SmsManager = if (simCard !=null){
                  SmsManager.getSmsManagerForSubscriptionId(simCard)
              }else{
                SmsManager.getDefault()
            }

            val parts = smsManager.divideMessage(message)
            smsManager.sendMultipartTextMessage(
                contact.phoneNumbers[0],
                sendingNumber,
                 parts,
                null,
                null)

            val updatedEntity = entity.copy(status = Enums.MessageStatus.SENT,
                from_id = sendingNumber)

            delay(1700)
            messageRepository.updateMessage(updatedEntity)


            emit(SendMessageState.Success)
        }
        catch (e: Exception)
        {
            val updatedEntity = entity.copy(status = Enums.MessageStatus.ERROR,
                from_id = sendingNumber)
            delay(1700)
            messageRepository.updateMessage(updatedEntity)
            emit(SendMessageState.Error(e.message))
        }

    }



    private suspend fun generatePlaceholderConversation(contact: Contact): Conversation {

        val conversationEntity:Conversation =  conversationRepository.getConversation(from =  PLACEHOLDER_CONVERSATION,
            to = contact.phoneNumbers[0])?: Conversation(
                from = PLACEHOLDER_CONVERSATION,
                to = contact.phoneNumbers[0],
                dateCreated = Timestamp( System.currentTimeMillis())
            )

        if (conversationRepository.getConversation(from = PLACEHOLDER_CONVERSATION, to = contact.phoneNumbers[0]) ==null)
            conversationRepository.addConversation(conversationEntity)
        return conversationEntity
    }



    private suspend  fun generateNewSenderConversation(sendingNumber: String, to :String){

        val conversationEntity:Conversation =  conversationRepository.getConversation(from =  sendingNumber,
            to =to)?: Conversation(
            from = sendingNumber,
            to = to,
            dateCreated = Timestamp( System.currentTimeMillis())
        )
        if (conversationRepository.getConversation(from = sendingNumber, to = to) ==null)
            conversationRepository.addConversation(conversationEntity)


    }



    private fun getSendingNumber(simCard: Int?): String? {
        Log.d("getSendingNumber", "called1")
        return try {
            val subscriptionManager = app.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
             val allSimId = getAllSimId()
              Log.d("getSendingNumber", "allSimId size =>  ${allSimId.size}")
              Log.d("getSendingNumber", "allSimId 1st element =>  ${allSimId[0]}")
            Log.d("getSendingNumber", "allSimId 1st element =>  ${allSimId[1]}")
            if (simCard != null) {
                Log.d("getSendingNumber", "called1.5 simCard: $simCard")
                val subscriptionInfo = subscriptionManager.getActiveSubscriptionInfo(simCard)
                Log.d("getSendingNumber", "called2 subscriptionInfo: ${subscriptionInfo.number}")
                Log.d("getSendingNumber", "called3 subscriptionInfo displayName: ${subscriptionInfo.displayName}")
                subscriptionInfo?.number

            } else {

                val defaultSubscription = SubscriptionManager.getDefaultSubscriptionId()
                val subscriptionInfo = subscriptionManager.getActiveSubscriptionInfo(defaultSubscription)
                subscriptionInfo?.number
            }
        } catch (e: SecurityException) {
            Log.d("getSendingNumber", "called4 subscriptionInfo: ${e.message}")
            null
        } catch (e: Exception) {
            Log.d("getSendingNumber", "called5 subscriptionInfo: ${e.message}")
            null
        }

    }
    private fun getAllSimId (): List<Int> { val subscriptionManager = app.getSystemService(
          Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
        return try {
            subscriptionManager.activeSubscriptionInfoList.map{
                it.subscriptionId
            }

        } catch (e: SecurityException) {
            Log.d("getAllSimId", "security exception")

            emptyList()
        } catch (e: Exception) {
            Log.d("getAllSimId", "general exception")
            emptyList()
        }
    }


}