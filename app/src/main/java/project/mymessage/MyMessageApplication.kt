package project.mymessage

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import androidx.work.DelegatingWorkerFactory
import dagger.hilt.android.HiltAndroidApp
import project.mymessage.domain.use_cases.chat.ReceiveMessageUseCase
import project.mymessage.domain.workers.factories.MyWorkerFactory
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



