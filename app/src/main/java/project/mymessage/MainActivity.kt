package project.mymessage


import android.app.Activity
import android.app.role.RoleManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Telephony
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import project.mymessage.database.MainDatabase
import project.mymessage.ui.theme.Grey300
import project.mymessage.ui.theme.Grey900
import project.mymessage.ui.theme.MyMessageTheme
import project.mymessage.ui.theme.RedLight
@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    private lateinit var intentLauncher: ActivityResultLauncher<Intent>

    companion object {
        // The requested role.
        const val role = RoleManager.ROLE_SMS
    }


    override fun onResume() {
        val prefs = androidx.preference.PreferenceManager.getDefaultSharedPreferences(baseContext)
        val previouslyStarted = prefs.getBoolean("previously started", false)
        if (!previouslyStarted) {
            val edit = prefs.edit()
            edit.putBoolean("previously started", java.lang.Boolean.TRUE)
            edit.commit()
            moveToMainActivity2()
        }
        super.onResume()
    }
    private fun moveToMainActivity2() {
        val i = Intent(baseContext, MainActivity2::class.java)
        startActivity(i)

    }



    @OptIn(ExperimentalPagerApi::class)
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setIntentLauncher()
        window.statusBarColor = ContextCompat.getColor(this, R.color.grey_900)
        setContent {
            MyMessageTheme {

                Surface(modifier = Modifier.fillMaxSize()) {
                    MainFunction()
                }
            }
        }
    }

    @ExperimentalPagerApi
    @Preview(showBackground = true)
    @Composable
    fun PreviewFunction(){
        Surface(modifier = Modifier.fillMaxSize()) {
            MainFunction()
        }
    }


    @ExperimentalPagerApi
    @Composable
    fun MainFunction(){
        val items = ArrayList<OnBoardingData>()

        items.add(
            OnBoardingData(
                R.raw.intro2,
                "Welcome",
                "Thank you for choosing MyMessage to send and receive SMSes, we aim to greatly improve your SMS  experience "
            )
        )

        items.add(
            OnBoardingData(
                R.raw.intro1,
                "Configuration",
                "Inorder for this app to work efficiently,  it needs to be set as the default SMS handler."
            )
        )

        items.add(
            OnBoardingData(
                R.raw.main_loader,
                "Activation ",
                "By clicking Get Started  you will be directed to the permissions page where you can set MyMessage as your default sms handler\n if this is not your wish then we are sorry but this app won't be much use to you. "
            )
        )
        val pagerState = rememberPagerState(
            pageCount = items.size,
            initialOffscreenLimit = 2,
            infiniteLoop = false,
            initialPage = 0
        )


        OnBoardingPager(
            item = items,
            pagerState = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
        )
    }

    @ExperimentalPagerApi
    @Composable
    fun rememberPagerState(
        @IntRange(from = 0) pageCount: Int,
        @IntRange(from = 0) initialPage: Int = 0,
        @FloatRange(from = 0.0, to = 1.0) initialPageOffset: Float = 0f,
        @IntRange(from = 1) initialOffscreenLimit: Int = 1,
        infiniteLoop: Boolean = false
    ): PagerState = rememberSaveable(
        saver = PagerState.Saver
    ) {
        PagerState(
            pageCount = pageCount,
            currentPage = initialPage,
            currentPageOffset = initialPageOffset,
            offscreenLimit = initialOffscreenLimit,
            infiniteLoop = infiniteLoop
        )
    }
    @ExperimentalPagerApi
    @Composable
    fun OnBoardingPager(
        item: List<OnBoardingData>,
        pagerState: PagerState,
        modifier: Modifier = Modifier,
    ) {
        Box(modifier = modifier) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HorizontalPager(
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
                                .align(alignment = Alignment.CenterHorizontally),item[page].image)
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

                PagerIndicator(item.size, pagerState.currentPage)
            }
            Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                BottomSection(pagerState)
            }
        }
    }



    @Composable
    fun PagerIndicator(
        size: Int,
        currentPage: Int
    ) {
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
        val width = animateDpAsState(targetValue = if (isSelected) 25.dp else 10.dp)

        Box(
            modifier = Modifier
                .padding(1.dp)
                .height(10.dp)
                .width(width.value)
                .clip(CircleShape)
                .background(
                    if (isSelected) RedLight else Grey300.copy(alpha = 0.5f)
                )
        )
    }

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    fun BottomSection(pagerState: PagerState) {
        val currentPager: Int = pagerState.currentPage
        Row(
            modifier = Modifier
                .padding(bottom = 20.dp)
                .fillMaxWidth(),
            horizontalArrangement = if (currentPager != 2) Arrangement.SpaceBetween else Arrangement.Center
        ) {
            if (currentPager == 2) {
                OutlinedButton(
                    onClick = { requestToBeDefaultHandler() },
                    shape = RoundedCornerShape(50),
                ) {
                    Text(
                        text = "Get Started",
                        modifier = Modifier
                            .padding(vertical = 8.dp, horizontal = 40.dp),
                        color = Grey900
                    )
                }
            } else {

                SkipNextButton(text = "Skip",
                    toPage = 2,
                    modifier = Modifier.padding(start = 20.dp),
                    pagerState= pagerState
                    )


                SkipNextButton(text = "Next",
                    toPage =currentPager+1,
                    modifier = Modifier.padding(end = 20.dp),
                    pagerState= pagerState
                )
            }
        }
    }

    private fun requestToBeDefaultHandler(){



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val roleManager: RoleManager = getSystemService(RoleManager::class.java)
            // check if the app is having permission to be as default SMS app
            val isRoleAvailable = roleManager.isRoleAvailable(role)
            if (isRoleAvailable) {
                // check whether your app is already holding the default SMS app role.
                val isRoleHeld = roleManager.isRoleHeld(role)
                if (!isRoleHeld) {
                    intentLauncher.launch(roleManager.createRequestRoleIntent(role))
                } else {
                    showToast("request permission here. ")
                }
            }
        } else {
            val intent = Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT)
            intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, packageName)
            startActivityForResult(intent, 1001)
        }

    }
    private fun setIntentLauncher() {
        intentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    showToast("Success requesting ROLE_SMS!")
                } else {
                    showToast("Failed requesting ROLE_SMS")
                }
            }
    }



    @OptIn(ExperimentalPagerApi::class)
    @Composable
   fun  SkipNextButton(text: String, modifier: Modifier, toPage: Int,pagerState: PagerState) {
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
            onClick =  {coroutineScope.launch {
                // Call scroll to on pagerState
                pagerState.scrollToPage(toPage)
            }}
        )
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
