package ih.tools.readingpad.feature_book_parsing.data

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.core.content.edit

/**
 * Manages preferences for the ReadingPad library using SharedPreferences.
 * Provides methods for setting and retrieving preferences related to font size, theme,colors, and other settings.
 *
 * @param context The application context.
 */
class PreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("reading_pad", Context.MODE_PRIVATE)

    /**
     * Sets the font size preference.
     *
     * @param fontSize The font size to set.
     */
    fun setFontSize(fontSize: Float) {
        with(sharedPreferences.edit()) {
            putFloat("fontSize", fontSize)
            apply()
        }
    }

    /*** Sets whether the top bar is pinned.
     *
     * @param isPinned True if the top bar should be pinned, false otherwise.
     */
    fun setPinnedTopBar(isPinned: Boolean) {
        sharedPreferences.edit { putBoolean("pinned_top_bar", isPinned) }
    }
    /**
     * Returns whether the top bar is pinned.
     *
     * @return True if the top bar is pinned, false otherwise.
     */
    fun isPinnedTopBar(): Boolean {
        return sharedPreferences.getBoolean("pinned_top_bar", false)
    }
    /**
     * Sets whether scrolling should be vertical.
     *
     * @param isVerticalScroll True for vertical scrolling, false otherwise.
     */
    fun setVerticalScroll(isVerticalScroll: Boolean) {
        sharedPreferences.edit { putBoolean("vertical_scroll", isVerticalScroll) }
    }
    /**
     * Returns whether scrolling is vertical.
     *
     * @return True if scrolling is vertical, false otherwise.
     */
    fun isVerticalScroll(): Boolean {
        return sharedPreferences.getBoolean("vertical_scroll", true)
    }

    /**
     * Sets whether the dark theme is enabled.
     *
     * @param isDarkTheme True for dark theme, false for light theme.
     */
    fun setDarkTheme(isDarkTheme: Boolean) {
        sharedPreferences.edit { putBoolean("dark_theme", isDarkTheme) }
    }
    /**
     * Returns whether the dark theme is enabled.
     *
     * @return True if dark theme is enabled, false otherwise.
     */
    fun isDarkTheme(): Boolean {
        return sharedPreferences.getBoolean("dark_theme", false)
    }

    /**
     * Returns the stored font size.
     *
     * @return The font size, or 16f if not set.
     */
    fun getFontSize(): Float {
        return sharedPreferences.getFloat("fontSize", 16f)
    }

    /**
     * Returns the stored font color.
     *
     * @return The font color as an ARGB integer, or black if not set.
     */
    fun getFontColor(): Int {
        return sharedPreferences.getInt("font_color", Color.Black.toArgb()) // Default font color: Black
    }

    /**
     * Sets the font color preference.
     *
     * @param color The font color as an ARGB integer.
     */
    fun setFontColor(color: Int) {
        sharedPreferences.edit().putInt("font_color", color).apply()
    }

    /**
     * Returns the stored font weight.
     *
     * @return The font weight as an integer, or FontWeight.Normal if not set.
     */
    fun getFontWeight() :Int{
        return sharedPreferences.getInt("font_weight", FontWeight.Normal.weight)
    }
    /**
     * Sets the font weight preference.
     *
     * @param fontWeight The font weight as an integer.
     */
    fun setFontWeight(fontWeight: Int){
        sharedPreferences.edit().putInt("font_weight", fontWeight).apply()
    }

    /**
     * Returns the stored background color.
     *
     * @return The background color as an ARGB integer, or white if not set.
     */
    fun getBackgroundColor(): Int {
        return sharedPreferences.getInt("background_color", Color.White.toArgb()) // Default background color: White
    }

    /**
     * Sets the background color preference.
     *
     * @param color The background color as an ARGB integer.
     */
    fun setBackgroundColor(color: Int) {
        sharedPreferences.edit().putInt("background_color", color).apply()
    }
}

