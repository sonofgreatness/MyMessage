package project.mymessage.util

import project.mymessage.database.Entities.Message
import project.mymessage.database.typeConverters.TimestampConverter
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class DatesManager {

companion object {

    /**
     * Gets date of the latest message by comparing timestamps.
     * @returns String - If the latest message is from today, return time in "HH:mm" format,
     * otherwise return the date in "dd MMM yyyy" format.
     */
    fun getLatestMessageDateFromMessages(messages: List<Message>): String {
        if (messages.isEmpty()) return "No messages"
        val latestMessage = messages.maxByOrNull { it.dateCreated.time } ?: return "No messages"
        val messageDate = Date(latestMessage.dateCreated.time)
        return  convertDateToString(messageDate)
    }

    fun convertTimestampToString(timez : Timestamp) : String {
        val finalDate = Date(timez.time)
        return  convertDateToString(finalDate)
    }

    private fun convertDateToString(finalDate : Date) : String{

        val today = Calendar.getInstance()
        val messageCalendar = Calendar.getInstance().apply { time = finalDate }
        return if (today.get(Calendar.YEAR) == messageCalendar.get(Calendar.YEAR) &&
            today.get(Calendar.DAY_OF_YEAR) == messageCalendar.get(Calendar.DAY_OF_YEAR)
        ) {
            SimpleDateFormat("HH:mm", Locale.getDefault()).format(finalDate)
        } else {
            SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(finalDate)
        }
    }
}


}