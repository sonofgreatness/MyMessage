package project.mymessage

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import project.mymessage.util.Constants.Companion.MYMESSAGE_TAG

@HiltAndroidApp
class MyMessageApplication : Application() {

    init {
        Log.d(MYMESSAGE_TAG,"Application  class started ")
    }

}