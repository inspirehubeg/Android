package ih.tools.readingpad.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

val ReadingPadLightColorScheme = lightColorScheme(
    primary = LightBrown,
    onPrimary = Beige,
    primaryContainer = Color(0xFFC5C4C7),
    onPrimaryContainer = Color(0xFF21005D),
    secondary = Color(0xFF03DAC6),
    onSecondary = Color(0xFF000000),
    secondaryContainer = Beige,
    onSecondaryContainer = Brown,
    tertiary = LightBeige,
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFFD8E4),
    onTertiaryContainer = Color(0xFF31111D),
    error = Color(0xFFB3261E),
    errorContainer = Color(0xFFF9DEDC),
    onError = Color(0xFFFFFFFF),
    onErrorContainer = Color(0xFF410E0B),
    background = Color(0xFFFFFBFE),
    onBackground = Color(0xFF1C1B1F),
    surface = LightBackground,
    onSurface = DarkGray,
    surfaceVariant = Color(0xFFDDDCDF),
    onSurfaceVariant = Color(0xFF49454F),
    outline = Color(0xFF79747E),
    inverseOnSurface = Color(0xFFF4EFF4),
    inverseSurface = Color(0xFF313033),
    inversePrimary = Color(0xFFD0BCFF),
    surfaceTint = Color(0xFF6200EE),
    outlineVariant = Color(0xFFCAC4D0),
    scrim = Color(0xFF000000),
)


val ReadingPadDarkColorScheme = darkColorScheme(
    primary = Beige,
    onPrimary = LightBrown,
    primaryContainer = Color(0xFF353535),
    onPrimaryContainer = Color(0xFFEADDFF),
    secondary = Color(0xFF03DAC6),
    onSecondary = Color(0xFF003937),
    secondaryContainer = LightBrown,
    onSecondaryContainer = Beige,
    tertiary = BookmarkDialogBrown,
    onTertiary = Color(0xFF492532),
    tertiaryContainer = Color(0xFF633B48),
    onTertiaryContainer = Color(0xFFFFD8E4),
    error = Color(0xFFF2B8B5),
    errorContainer = Color(0xff8C1D18),
    onError = Color(0xFF601410),
    onErrorContainer = Color(0xFFF9DEDC),
    background = Color(0xFF1C1B1F),
    onBackground = Color(0xFFE6E1E5),
    surface = DarkBackground,
    onSurface = LightGray,
    surfaceVariant = Color(0xFF49454F),
    onSurfaceVariant = Color(0xFFCAC4D0),
    outline = Color(0xFF938F99),
    inverseOnSurface = Color(0xFF1C1B1F),
    inverseSurface = Color(0xFFE6E1E5),
    inversePrimary = Color(0xFF6200EE),
    surfaceTint =Color(0xFFBB86FC),
    outlineVariant = Color(0xFF49454F),
    scrim = Color(0xFF000000),
)
@Composable
fun ReadingPadTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme =
            if (darkTheme) ReadingPadDarkColorScheme else ReadingPadLightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        val window = (view.context as Activity).window
        val insetsController = WindowCompat.getInsetsController(window, view)
//        insetsController.apply {
//            hide(WindowInsetsCompat.Type.statusBars())
//            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
//        }
        SideEffect {
           // val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.secondaryContainer.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )


}
// Object to hold your theme
object ReadingPadTheme {
    val colorScheme: ColorScheme
        @Composable
        get() = MaterialTheme.colorScheme
}