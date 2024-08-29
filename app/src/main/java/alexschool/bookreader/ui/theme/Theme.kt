package alexschool.bookreader.ui.theme

import alexschool.bookreader.R
import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    surfaceContainer = Color.DarkGray


)

private val LightColorScheme = lightColorScheme(
    primary = Purple80,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    surfaceContainer = Color.LightGray


    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

val defaultTypography = Typography(
    // ... your default font family settings
)

val customTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.bookerly_regular, FontWeight.Normal)),
        fontWeight = FontWeight.Normal,
    ),
    displayMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.bookerly_regular, FontWeight.Normal)),
        fontWeight = FontWeight.Normal,
    ),
    displaySmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.bookerly_regular, FontWeight.Normal)),
        fontWeight = FontWeight.Normal,
    ),
    headlineLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.bookerly_regular, FontWeight.Normal)),
        fontWeight = FontWeight.Normal,
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.bookerly_regular, FontWeight.Normal)),
        fontWeight = FontWeight.Normal,
    ),
    headlineSmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.bookerly_regular, FontWeight.Normal)),
        fontWeight = FontWeight.Normal,
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.bookerly_regular, FontWeight.Normal)),
        fontWeight = FontWeight.Normal,
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.bookerly_regular, FontWeight.Normal)),
        fontWeight = FontWeight.Normal,
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.bookerly_regular, FontWeight.Normal)),
        fontWeight = FontWeight.Normal,
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.bookerly_regular, FontWeight.Normal)),
        fontWeight = FontWeight.Normal,
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.bookerly_regular, FontWeight.Normal)),
        fontWeight = FontWeight.Normal,
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.bookerly_regular, FontWeight.Normal)),
        fontWeight = FontWeight.Normal,
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.bookerly_regular, FontWeight.Normal)),
        fontWeight = FontWeight.Normal,
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.bookerly_regular, FontWeight.Normal)),
        fontWeight = FontWeight.Normal,
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.bookerly_regular, FontWeight.Normal)),
        fontWeight = FontWeight.Normal,
    )
)

@Composable
fun AlexSchoolTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.surfaceContainer.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}