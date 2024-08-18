package alexschool.bookreader.ui.settings

import alexschool.bookreader.R
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

fun updateAppLocale(context: Context, language: String) {

    //val locale = LocaleListCompat.forLanguageTags(language)

    // extra logic you might want to execute

    Log.d("MainActivity", "updateAppLocale: $language")
    val locale = when (language) {
        context.getString(R.string.english) -> LocaleListCompat.forLanguageTags("en")
        context.getString(R.string.arabic) -> LocaleListCompat.forLanguageTags("ar")
        else -> LocaleListCompat.getDefault()
    }
    AppCompatDelegate.setApplicationLocales(locale)
    //Locale.setDefault(locale)
}