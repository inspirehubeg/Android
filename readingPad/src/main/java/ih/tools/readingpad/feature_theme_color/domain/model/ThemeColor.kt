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

@Entity(tableName = "theme_color_table")
data class ThemeColor(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, val argb: Int, val type: ThemeColorType
) {
    companion object {
        val preDefinedFontColors = listOf(
            Color.Blue,
            Color.Black,
        )
        val preDefinedBackgroundColors = listOf(
            Peach, Orange, Yellow, Purple, LightRed, Turquoise, BlueGrey, Grey, Blue, Green
        )
    }
}

enum class ThemeColorType {
    BACKGROUND, FONT
}
