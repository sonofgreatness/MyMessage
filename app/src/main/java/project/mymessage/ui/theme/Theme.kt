package project.mymessage.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import project.mymessage.util.Enums

// Light theme approximation of One UI
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF007AFF),
    onPrimary = Color.White,
    secondary = Color(0xFF5856D6),
    onSecondary = Color.White,
    background = Color(0xFFFFFFFF),
    onBackground = Color(0xFF000000),
    surface = Color(0xFFF2F2F2),
    onSurface = Color(0xFF000000)
)

// Dark theme approximation of One UI
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF0A84FF),
    onPrimary = Color.Black,
    secondary = Color(0xFF5E5CE6),
    onSecondary = Color.Black,
    background = Color(0xFF121212),
    onBackground = Color(0xFFFFFFFF),
    surface = Color(0xFF1E1E1E),
    onSurface = Color(0xFFFFFFFF)
)

@Composable
fun MyMessageTheme(
    themePref: String,
                   content: @Composable () -> Unit) {

    val darkTheme = when (themePref) {
        Enums.ThemeMode.NIGHT.name ->  true
        Enums.ThemeMode.DAY.name -> false
        Enums.ThemeMode.NOTSET.name -> isSystemInDarkTheme()
        else -> {isSystemInDarkTheme()}
    }
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        shapes = Shapes(),
        content = content
    )
}