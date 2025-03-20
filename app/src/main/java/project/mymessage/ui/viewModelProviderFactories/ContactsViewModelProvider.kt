package project.mymessage.ui.viewModelProviderFactories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import project.mymessage.ui.viewModels.ContactsViewModel
import javax.inject.Inject

class ContactsViewModelProvider@Inject constructor(
    private val application: Application
) :ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T{
        return ContactsViewModel(application)
                as T
    }
}