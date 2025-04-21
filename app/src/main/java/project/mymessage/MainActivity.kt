package project.mymessage

import android.app.Activity
import android.app.role.RoleManager
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import project.mymessage.ui.theme.Grey300
import project.mymessage.ui.theme.MyMessageTheme
import project.mymessage.ui.theme.RedLight
import project.mymessage.ui.viewModels.AboutViewModel
import project.mymessage.util.Constants.Companion.THEME_PREFS
import project.mymessage.util.Enums
import kotlin.getValue

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mAboutViewModel: AboutViewModel by viewModels()
    private lateinit var intentLauncher: ActivityResultLauncher<Intent>

    companion object {
        const val role = RoleManager.ROLE_SMS
    }

    override fun onResume() {
        val prefs = androidx.preference.PreferenceManager.getDefaultSharedPreferences(baseContext)
        val previouslyStarted = prefs.getBoolean("previously started", false)
        if (!previouslyStarted) {
            val edit = prefs.edit()
            edit.putBoolean("previously started", true)
            edit.apply()
            moveToMainActivity2()
        }
        super.onResume()
    }

    private fun moveToMainActivity2() {
        val i = Intent(baseContext, MainActivity2::class.java)
        startActivity(i)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setIntentLauncher()
        window.statusBarColor = ContextCompat.getColor(this, R.color.grey_900)
        setContent {
            MyMessageTheme(themePref= mAboutViewModel.sharedPreferences.getString(THEME_PREFS, Enums.ThemeMode.NOTSET.name)
                .toString()) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainFunction()
                }
            }
        }
    }

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    fun MainFunction() {
        val items = listOf(
            OnBoardingData(R.raw.intro2, "Welcome", "Thank you for choosing MyMessage..."),
            OnBoardingData(R.raw.intro1, "Configuration", "In order for this app to work..."),
            OnBoardingData(R.raw.main_loader, "Activation", "By clicking Get Started...")
        )

        val pagerState = rememberPagerState()

        OnBoardingPager(
            item = items,
            pagerState = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        )
    }

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    fun OnBoardingPager(
        item: List<OnBoardingData>,
        pagerState: PagerState,
        modifier: Modifier = Modifier,
    ) {
        Box(modifier = modifier) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                HorizontalPager(
                    count = item.size,
                    state = pagerState
                ) { page ->
                    Column(
                        modifier = Modifier
                            .padding(60.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        LoaderIntro(
                            modifier = Modifier
                                .size(200.dp)
                                .fillMaxWidth()
                                .align(Alignment.CenterHorizontally),
                            item[page].image
                        )
                        Text(
                            text = item[page].title,
                            modifier = Modifier.padding(top = 50.dp),
                            color = Color.Black,
                            style = MaterialTheme.typography.headlineSmall,
                        )
                        Text(
                            text = item[page].desc,
                            modifier = Modifier.padding(top = 30.dp, start = 20.dp, end = 20.dp),
                            color = Color.Black,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                PagerIndicator(size = item.size, currentPage = pagerState.currentPage)
            }
            Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                BottomSection(pagerState)
            }
        }
    }

    @Composable
    fun PagerIndicator(size: Int, currentPage: Int) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(top = 60.dp)
        ) {
            repeat(size) {
                Indicator(isSelected = it == currentPage)
            }
        }
    }

    @Composable
    fun Indicator(isSelected: Boolean) {
        val width = animateDpAsState(if (isSelected) 25.dp else 10.dp, label = "")
        Box(
            modifier = Modifier
                .padding(1.dp)
                .height(10.dp)
                .width(width.value)
                .clip(CircleShape)
                .background(if (isSelected) RedLight else Grey300.copy(alpha = 0.5f))
        )
    }

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    fun BottomSection(pagerState: PagerState) {
        val currentPage = pagerState.currentPage
        Row(
            modifier = Modifier
                .padding(bottom = 20.dp)
                .fillMaxWidth(),
            horizontalArrangement = if (currentPage != 2) Arrangement.SpaceBetween else Arrangement.Center
        ) {
            if (currentPage == 2) {
                OutlinedButton(
                    onClick = { moveToMainActivity2() },
                    shape = RoundedCornerShape(50),
                    colors =  ButtonDefaults. outlinedButtonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White

                    )
                ) {
                    Text(
                        text = "Get Started",
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 40.dp),
                    )
                }
            } else {
                SkipNextButton("Skip", Modifier.padding(start = 20.dp), toPage = 2, pagerState)
                SkipNextButton("Next", Modifier.padding(end = 20.dp), toPage = currentPage + 1, pagerState)
            }
        }
    }

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    fun SkipNextButton(text: String, modifier: Modifier, toPage: Int, pagerState: PagerState) {
        val coroutineScope = rememberCoroutineScope()
        ClickableText(
            text = AnnotatedString(text),
            modifier = modifier,
            style = TextStyle(
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = MaterialTheme.typography.bodyLarge.fontFamily
            ),
            onClick = {
                coroutineScope.launch {
                    pagerState.scrollToPage(toPage)
                }
            }
        )
    }


    private fun setIntentLauncher() {
        intentLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                showToast("Success requesting ROLE_SMS!")
            } else {
                showToast("Failed requesting ROLE_SMS")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
