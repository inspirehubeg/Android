package alexschool.bookreader.data

import alexschool.bookreader.R
import android.content.Context

class AlexSchoolPrefManager(private val context: Context) {

    private val sharedPreferences =
        context.getSharedPreferences("alex_school_prefs", Context.MODE_PRIVATE)

    fun setLanguage(language: String) {
        with(sharedPreferences.edit()) {
            putString(LANGUAGE_KEY, language)
            apply()
        }
    }

    fun getLanguage(): String {
        return sharedPreferences.getString(LANGUAGE_KEY, context.getString(R.string.system_default))
            ?: context.getString(R.string.system_default)
    }

    fun setVersion(version: Long) {
        with(sharedPreferences.edit()) {
            putLong(VERSION_KEY, version)
            apply()
        }
    }
    fun getVersion(): Long {
        return sharedPreferences.getLong(VERSION_KEY, 0)
    }


    companion object {
        private const val LANGUAGE_KEY = "language"
        private const val VERSION_KEY = "version"
    }
}