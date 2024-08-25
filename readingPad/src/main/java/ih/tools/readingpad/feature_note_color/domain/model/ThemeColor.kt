package ih.tools.readingpad.feature_note_color.domain.model

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import ih.tools.readingpad.ui.theme.Purple80

@Entity(tableName = "theme_color_table")
data class ThemeColor(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val argb: Int,
    val type: ThemeColorType
){
    companion object {
        val preDefinedFontColors = listOf(Color.Blue, Color.Black,Color.Blue, Color.Black,Color.Blue, Color.Black,Color.Blue, Color.Black,Color.Blue, Color.Black)
        val preDefinedBackgroundColors = listOf(Purple80)
    }
}

enum class ThemeColorType {
    BACKGROUND,FONT
}
