package ih.tools.readingpad.feature_theme_color.domain.model

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import ih.tools.readingpad.ui.theme.Blue
import ih.tools.readingpad.ui.theme.BlueGrey
import ih.tools.readingpad.ui.theme.Green
import ih.tools.readingpad.ui.theme.Grey
import ih.tools.readingpad.ui.theme.LightRed
import ih.tools.readingpad.ui.theme.Orange
import ih.tools.readingpad.ui.theme.Peach
import ih.tools.readingpad.ui.theme.Purple
import ih.tools.readingpad.ui.theme.Turquoise
import ih.tools.readingpad.ui.theme.Yellow

/**
 * Represents a color theme with an associated type (background or font).
 *
 * @property id The unique identifier for the theme color (auto-generated).
 * @property argb The color value in ARGB format.
 * @property type The type of the theme color (background or font).
 */
@Entity(tableName = "theme_color_table")
data class ThemeColor(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, val argb: Int, val type: ThemeColorType
) {
    companion object {
        /**
         * Predefined list of font colors.
         * Consider using more descriptive color names or hex codes for better readability.
         */
        val preDefinedFontColors = listOf(
            Color.Blue,
            Color.Black,
        )

        /**
         * Predefined list of background colors.
         * Consider using more descriptive color names or hex codes for better readability.
         * Ensure all color values are defined (e.g., Peach, Orange, Yellow, etc.).
         */
        val preDefinedBackgroundColors = listOf(
            Peach, Orange, Yellow, Purple, LightRed, Turquoise, BlueGrey, Grey, Blue, Green
        )
    }
}
/**
 * Enum class representing the type of a theme color.
 */
enum class ThemeColorType {
    BACKGROUND, FONT
}
