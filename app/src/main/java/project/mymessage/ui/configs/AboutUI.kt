package project.mymessage.ui.configs

import android.R
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import project.mymessage.ui.viewModels.AboutViewModel
import project.mymessage.util.Enums

class AboutUI {

    companion object {


        @Composable
        fun AboutScreen(
            viewModel: AboutViewModel,
            navController: NavController,

        ) {
            val isUpdateAvailable by viewModel.isUpdateAvailable.observeAsState(false)
            val latestVersion by viewModel.latestVersion.observeAsState("Unknown")
             val currentTheme by viewModel.isDarkTheme.observeAsState()
             val isDarkTheme: Boolean  = currentTheme == Enums.ThemeMode.NIGHT



            LaunchedEffect(Unit) {
                viewModel.checkForUpdates()
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier
                                .size(36.dp)
                                .clickable { navController.popBackStack() }
                        )

                        Text(
                            text = "About My Messages",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = MaterialTheme.colorScheme.onBackground
                            ),
                            modifier = Modifier.padding(start = 8.dp)
                        )

                        Spacer(modifier = Modifier.weight(1f))
                        ThemeToggleButton(isDarkTheme = isDarkTheme, onToggle = {viewModel.toggleTheme()})
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Current Version: ${
                            viewModel.sharedPreferences.getString("app_version", "v1.0")
                        }",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Latest Version: $latestVersion",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (isUpdateAvailable) {
                        OutlinedButton(
                            onClick = { viewModel.downloadUpdate() },
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = MaterialTheme.colorScheme.onBackground,
                                contentColor = MaterialTheme.colorScheme.background
                            )
                        ) {
                            Text(
                                text = "Download Update",
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 40.dp),
                                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.background)
                            )
                        }
                    } else {
                        Text(
                            text = "You're up to date!",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    ClickableText(
                        text = AnnotatedString("Visit Project Site"),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.primary,
                            textDecoration = TextDecoration.Underline
                        ),
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://sonofgreatness.github.io/MyMessage/"))
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            navController.context.startActivity(intent)
                        }
                    )
                }
            }
        }

        @Composable
        fun GitHubLink(link: String) {
            val context = LocalContext.current
            Text(
                text = "View on GitHub",
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .clickable {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                        context.startActivity(intent)
                    }
                    .padding(vertical = 8.dp)
            )
        }

        @Composable
        fun ThemeToggleButton(isDarkTheme: Boolean, onToggle: () -> Unit) {
            val rotation by animateFloatAsState(
                targetValue = if (isDarkTheme) 180f else 0f,
                label = "iconRotation"
            )

            IconButton(onClick = onToggle) {
                val iconResId = if (isDarkTheme) {
                    project.mymessage.R.drawable.baseline_dark_mode_24
                } else {
                    project.mymessage.R.drawable.baseline_light_mode_24
                }

                Icon(
                    painter = painterResource(id = iconResId),
                    contentDescription = "Toggle Theme",
                    modifier = Modifier
                        .size(32.dp)
                        .rotate(rotation),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }



    }
}