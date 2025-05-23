package project.mymessage

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import project.mymessage.ui.nav.Navigation
import project.mymessage.ui.theme.MyMessageTheme
import project.mymessage.ui.viewModels.AboutViewModel
import project.mymessage.ui.viewModels.ContactsViewModel
import project.mymessage.ui.viewModels.ConversationViewModel
import project.mymessage.ui.viewModels.SearchViewModel
import project.mymessage.util.Constants.Companion.THEME_PREFS
import project.mymessage.util.Enums


@AndroidEntryPoint
class MainActivity2 : ComponentActivity() {
    private val mConversationViewModel: ConversationViewModel by viewModels()
    private val mContactsViewModel: ContactsViewModel by viewModels()
    private val mSearchViewModel: SearchViewModel by viewModels()
    private val mAboutViewModel: AboutViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.grey_900)

        val rawTheme = mAboutViewModel.sharedPreferences.getString(THEME_PREFS, Enums.ThemeMode.NOTSET.name)
            .toString()
        Log.d("toggleTheme.", "onCreate: $rawTheme")
        setContent {
            MyMessageTheme(themePref= mAboutViewModel.sharedPreferences.getString(THEME_PREFS, Enums.ThemeMode.NOTSET.name)
                .toString()) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Navigation(mConversationViewModel, mContactsViewModel, mSearchViewModel, mAboutViewModel)
                }
            }
        }
        mContactsViewModel.checkAndRequestPermissions(this)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mContactsViewModel.updatePermissionStatus(true)
        } else {
            mContactsViewModel.updatePermissionStatus(false)
        }
    }
}
