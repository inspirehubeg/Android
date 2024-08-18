package alexschool.bookreader.ui.settings

import android.app.Activity
import android.content.res.Configuration
import android.util.Log
import java.util.Locale

fun updateAppLocale(activity: Activity, language: String) {
    Log.d("MainActivity", "updateAppLocale: $language")
    val locale = when (language) {
        "System default" -> Locale.getDefault()
        "English" -> Locale("en")
        "Arabic" -> Locale("ar")
        else -> Locale.getDefault()
    }
    Locale.setDefault(locale)
    val config = Configuration(activity.resources.configuration)
    config.setLocale(locale)
    activity.createConfigurationContext(config)
}