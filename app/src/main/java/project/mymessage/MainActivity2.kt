package project.mymessage

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import project.mymessage.ui.nav.Navigation
import project.mymessage.ui.theme.MyMessageTheme
import project.mymessage.ui.viewModels.ContactsViewModel
import project.mymessage.ui.viewModels.ConversationViewModel
import project.mymessage.ui.viewModels.SearchViewModel

@AndroidEntryPoint
class MainActivity2 : ComponentActivity() {
    private val mConversationViewModel: ConversationViewModel by viewModels()
    private val mContactsViewModel: ContactsViewModel by viewModels()
     private  val mSearchViewModel :SearchViewModel by viewModels ()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.grey_900)
        setContent {

            MyMessageTheme {

                Surface(modifier = Modifier.fillMaxSize()) {
                    Navigation(mConversationViewModel, mContactsViewModel, mSearchViewModel)
                }
            }


        }
        mContactsViewModel.checkAndRequestPermissions(this)
    }

    // Handle permission results
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
