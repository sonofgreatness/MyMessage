package project.mymessage.domain.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import project.mymessage.domain.use_cases.chat.ReceiveMessageUseCase
import javax.inject.Inject

class IncomingSmsWorker  @Inject
     constructor(
        private val receiveMessageUseCase: ReceiveMessageUseCase,
      context: Context,
      workerParams: WorkerParameters
  )
    : CoroutineWorker(context, workerParams) {

        override suspend fun doWork(): Result {
            val sender = inputData.getString("sender") ?: return Result.failure()
            val messageBody = inputData.getString("message_body") ?: return Result.failure()

        return try {
            receiveMessageUseCase(sender, messageBody).collect {
                Log.d("IncomingSmsWorker", "Message received: $it")
            }
            Result.success()
        } catch (e: Exception) {
            Log.e("IncomingSmsWorker", "Error: ${e.message}", e)
            Result.failure()
        }
    }
}

