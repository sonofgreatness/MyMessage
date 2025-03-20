package project.mymessage.util

import project.mymessage.database.Entities.Message

class ConversationsManager  {

    companion object {
        fun getNumberOfUnreadMessages(messages: List<Message>): Int {
            var sum = 0
            messages.forEach {
                if (!it.isRead)
                    sum += 1
            }
            return sum
        }
    }
}