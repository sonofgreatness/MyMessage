package project.mymessage.ui.configs

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import project.mymessage.ui.viewModels.AboutViewModel

class AboutUI {

    companion object{


        @Composable
        fun AboutScreen(viewModel: AboutViewModel) {
            LaunchedEffect(Unit) {
                viewModel.checkForUpdates()
            }
            val isUpdateAvailable  by viewModel.isUpdateAvailable.observeAsState(false)
            val latesVersion  by viewModel.latestVersion.observeAsState("Unknown")
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)) {
                Text(text = "About My Messages", style = MaterialTheme.typography.overline)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Current Version: ${viewModel.sharedPreferences.getString("app_version", "v1.0")}")
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Latest Version: $latesVersion")
                Spacer(modifier = Modifier.height(16.dp))

                if (isUpdateAvailable) {
                    Button(onClick = { viewModel.downloadUpdate() }) {
                        Text(text = "Download Update")
                    }
                } else {
                    Text(text = "You're up to date!", color = MaterialTheme.colors.primary)
                }
            }
        }

    }
}