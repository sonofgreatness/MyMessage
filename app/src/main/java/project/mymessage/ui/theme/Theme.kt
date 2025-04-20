package project.mymessage.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Light theme approximation of One UI
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF007AFF),         // Samsung blue
    onPrimary = Color.White,
    secondary = Color(0xFF5856D6),       // Deep purple
    onSecondary = Color.White,
    background = Color(0xFFFFFFFF),
    onBackground = Color(0xFF000000),
    surface = Color(0xFFF2F2F2),         // Light gray surface
    onSurface = Color(0xFF000000)
)

// Dark theme approximation of One UI
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF0A84FF),         // Lighter blue for dark background
    onPrimary = Color.Black,
    secondary = Color(0xFF5E5CE6),       // Purple tone
    onSecondary = Color.Black,
    background = Color(0xFF121212),
    onBackground = Color(0xFFFFFFFF),
    surface = Color(0xFF1E1E1E),
    onSurface = Color(0xFFFFFFFF)
)

@Composable
fun MyMessageTheme(darkTheme: Boolean = isSystemInDarkTheme(),
                   content: @Composable () -> Unit) {

    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        shapes = Shapes(),
        content = content
    )
}