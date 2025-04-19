package project.mymessage

import android.app.Application
import android.util.Log
import androidx.compose.ui.platform.LocalFocusManager
import androidx.work.Configuration
import androidx.work.Data
import androidx.work.DelegatingWorkerFactory
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import dagger.hilt.android.HiltAndroidApp
import project.mymessage.domain.use_cases.chat.ReceiveMessageUseCase
import project.mymessage.domain.workers.IncomingSmsWorker
import project.mymessage.domain.workers.factories.MyWorkerFactory
import java.util.UUID
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Inject


@HiltAndroidApp
class MyMessageApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var receiveMessageUseCase: ReceiveMessageUseCase
    override fun onCreate() {
        super.onCreate()
        Log.d("MyMessageApplication", "App onCreate called")
    }
    override fun getWorkManagerConfiguration(): Configuration {

        Log.d("MyOverride","called")
        val myWorkerFactory = DelegatingWorkerFactory()
        myWorkerFactory.addFactory(MyWorkerFactory(receiveMessageUseCase))

        return   Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setWorkerFactory(
                MyWorkerFactory(receiveMessageUseCase))
            .build()

    }
}



