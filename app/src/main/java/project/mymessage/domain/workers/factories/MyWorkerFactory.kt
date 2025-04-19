package project.mymessage.domain.workers.factories

import android.content.Context
import android.util.Log
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import project.mymessage.domain.use_cases.chat.ReceiveMessageUseCase
import project.mymessage.domain.workers.IncomingSmsWorker


class MyWorkerFactory(
        private val receiveMessageUseCase: ReceiveMessageUseCase
    ) : WorkerFactory() {

        override fun createWorker(
            appContext: Context,
            workerClassName: String,
            workerParameters: WorkerParameters
        ): ListenableWorker? {
            Log.d("MyWorkerFactory", "createWorker called")
            return when(workerClassName) {
                IncomingSmsWorker::class.java.name ->
                    IncomingSmsWorker(receiveMessageUseCase, appContext,
                        workerParameters)
                else ->
                    null
            }
        }


    }
