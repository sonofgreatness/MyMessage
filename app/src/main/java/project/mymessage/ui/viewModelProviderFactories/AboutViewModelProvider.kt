package project.mymessage.ui.viewModelProviderFactories

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import project.mymessage.domain.repository.REST_API.interfaces.GithubApi
import javax.inject.Inject

class AboutViewModelProvider
 @Inject constructor(
     private val applicationContext: Application,
     private val sharedPreferences: SharedPreferences,
     private val api: GithubApi
)  : ViewModelProvider.Factory
{

    override fun <T : ViewModel> create(modelClass: Class<T>): T{
        return AboutViewModelProvider(applicationContext, sharedPreferences,api)
                as T
    }
}