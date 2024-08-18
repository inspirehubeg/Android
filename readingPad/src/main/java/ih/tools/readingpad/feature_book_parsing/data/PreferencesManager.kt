package ih.tools.readingpad.feature_book_parsing.data

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.core.content.edit


class PreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("reading_pad", Context.MODE_PRIVATE)


    fun setFontSize(fontSize: Float) {
        with(sharedPreferences.edit()) {
            putFloat("fontSize", fontSize)
            apply()
        }
    }

    fun setPinnedTopBar(isPinned: Boolean) {
        sharedPreferences.edit { putBoolean("pinned_top_bar", isPinned) }
    }

    fun isPinnedTopBar(): Boolean {
        return sharedPreferences.getBoolean("pinned_top_bar", false)
    }

    fun setVerticalScroll(isVerticalScroll: Boolean) {
        sharedPreferences.edit { putBoolean("vertical_scroll", isVerticalScroll) }
    }
    fun isVerticalScroll(): Boolean {
        return sharedPreferences.getBoolean("vertical_scroll", true)
    }

    fun setDarkTheme(isDarkTheme: Boolean) {
        sharedPreferences.edit { putBoolean("dark_theme", isDarkTheme) }
    }

    fun isDarkTheme(): Boolean {
        return sharedPreferences.getBoolean("dark_theme", false)
    }
    fun getFontSize(): Float {
        return sharedPreferences.getFloat("fontSize", 16f)
    }

    fun getFontColor(): Int {
        return sharedPreferences.getInt("font_color", Color.Black.toArgb()) // Default font color: Black
    }

    fun setFontColor(color: Int) {
        sharedPreferences.edit().putInt("font_color", color).apply()
    }

    fun getFontWeight() :Int{
        return sharedPreferences.getInt("font_weight", FontWeight.Normal.weight)
    }

    fun setFontWeight(fontWeight: Int){
        sharedPreferences.edit().putInt("font_weight", fontWeight).apply()
    }

    fun getBackgroundColor(): Int {
        return sharedPreferences.getInt("background_color", Color.White.toArgb()) // Default background color: White
    }

    fun setBackgroundColor(color: Int) {
        sharedPreferences.edit().putInt("background_color", color).apply()
    }
}

