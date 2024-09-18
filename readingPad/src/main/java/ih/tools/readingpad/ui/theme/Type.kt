package ih.tools.readingpad.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ih.tools.readingpad.R

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)
val bookerlyFont = FontFamily(
    Font(R.font.bookerly_regular, FontWeight.Normal),
    Font(R.font.bookerly_bold, FontWeight.Bold),
    Font(R.font.bookerly_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.bookerly_bold_italic, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.bookerly_light, FontWeight.Light),
    Font(R.font.bookerly_light_italic, FontWeight.Light, FontStyle.Italic),
)