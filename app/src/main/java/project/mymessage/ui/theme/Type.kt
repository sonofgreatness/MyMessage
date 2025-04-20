package project.mymessage.ui.theme


import androidx.compose.material3.Typography
import androidx.compose.runtime.Stable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import project.mymessage.R

// Set of Material typography styles to start with
val jostFontFamily = FontFamily(
    Font(R.font.jost_normal, FontWeight.Normal),
    Font(R.font.jost_light, FontWeight.Light),
    Font(R.font.jost_medium, FontWeight.Medium),
    Font(R.font.jost_semibold, FontWeight.SemiBold),
    Font(R.font.jost_bold, FontWeight.Bold),
)

// Set of Material typography styles to start with
val Typography = Typography(

    displayLarge = TextStyle(
        fontFamily = jostFontFamily,
        fontWeight = FontWeight.Light,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = 1.sp
    ),
    displayMedium = TextStyle(
        fontFamily = jostFontFamily,
        fontWeight = FontWeight.Light,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 1.sp
    ),
    displaySmall = TextStyle(
        fontFamily = jostFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 1.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = jostFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 1.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = jostFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 1.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = jostFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 1.sp
    ),
    titleLarge = TextStyle(
        fontFamily = jostFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 1.sp
    ),
    titleMedium = TextStyle(
        fontFamily = jostFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 1.15.sp
    ),
    titleSmall = TextStyle(
        fontFamily = jostFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 1.1.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = jostFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 1.15.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = jostFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 1.25.sp
    ),
    bodySmall = TextStyle(
        fontFamily = jostFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 1.4.sp
    ),
    labelLarge = TextStyle(
        fontFamily = jostFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 1.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = jostFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 1.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = jostFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 1.5.sp
    )
)
@Stable
val Typography.h1: TextStyle
    get() = displayLarge

@Stable
val Typography.h2: TextStyle
    get() = displayMedium

@Stable
val Typography.h3: TextStyle
    get() = displaySmall

@Stable
val Typography.h4: TextStyle
    get() = headlineLarge

@Stable
val Typography.h5: TextStyle
    get() = headlineMedium

@Stable
val Typography.h6: TextStyle
    get() = titleLarge

@Stable
val Typography.subtitle1: TextStyle
    get() = titleMedium

@Stable
val Typography.subtitle2: TextStyle
    get() = titleSmall

@Stable
val Typography.body1: TextStyle
    get() = bodyLarge

@Stable
val Typography.body2: TextStyle
    get() = bodyMedium

@Stable
val Typography.button: TextStyle
    get() = labelLarge

@Stable
val Typography.caption: TextStyle
    get() = labelMedium

@Stable
val Typography.overline: TextStyle
    get() = labelSmall