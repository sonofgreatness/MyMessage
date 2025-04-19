package project.mymessage.domain.use_cases.chat


import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import project.mymessage.database.Entities.Conversation
import project.mymessage.database.Entities.Message
import project.mymessage.domain.repository.interfaces.ConversationRepository
import project.mymessage.domain.repository.interfaces.MessageRepository
import project.mymessage.util.Enums
import java.sql.Timestamp
import java.util.*
import javax.inject.Inject

class ReceiveMessageUseCase @Inject constructor(
    private val messageRepository: MessageRepository,
    private val conversationRepository: ConversationRepository
) {

    operator fun invoke(sender: String, body: String): Flow<Unit> = flow {
        val msgId = UUID.randomUUID()

        // Step 1: Ensure conversation exists
        val conversation = getOrCreateConversation(sender)

        // Step 2: Create Message entity
        val message = Message(
            id = msgId,
            from_id = sender,
            to_id = conversation.from, // receiver is your app/device number
            content = body,
            messageType = Enums.MessageType.Incoming.value,
            status = Enums.MessageStatus.DELIVERED,
            dateCreated = Timestamp(System.currentTimeMillis())
        )

        // Step 3: Save to DB
        messageRepository.addMessage(message)

        emit(Unit)
    }

    private suspend fun getOrCreateConversation(sender: String): Conversation {
        val existing = conversationRepository.getConversation(from = sender, to = sender)
        return if (existing != null) {
            existing
        } else {
            val newConv = Conversation(
                from = sender,
                to = sender,
                dateCreated = Timestamp(System.currentTimeMillis())
            )
            conversationRepository.addConversation(newConv)
            newConv
        }
    }
}
