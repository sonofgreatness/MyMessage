package project.mymessage

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import project.mymessage.domain.workers.IncomingSmsWorker

class MessageReadReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
          Log.d("MyBroadcastReceiver", "onReceive called")
            if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
                Log.d("MyBroadcastReceiver", "onReceive called2")
                val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
                val fullMessage = StringBuilder()
                for (smsMessage in messages) {
                    fullMessage.append(smsMessage.messageBody)
                }
                val sender = messages[0].originatingAddress ?: "Unknown"
                val workData = workDataOf(
                    "message_body" to fullMessage.toString(),
                    "sender" to sender
                )
                val workRequest = OneTimeWorkRequestBuilder<IncomingSmsWorker>()
                    .setInputData(workData)
                    .build()
                WorkManager.getInstance(context).enqueue(workRequest)
                Log.d("MyBroadcastReceiver", "onReceive called3")
            }
        }
}
