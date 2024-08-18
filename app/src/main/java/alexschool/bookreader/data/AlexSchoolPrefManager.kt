package alexschool.bookreader.data

import android.content.Context

class AlexSchoolPrefManager (private val context: Context) {

    private val sharedPreferences = context.getSharedPreferences("alex_school_prefs", Context.MODE_PRIVATE)

    fun setLanguage(language: String) {
        with(sharedPreferences.edit()){
            putString(LANGUAGE_KEY, language)
            apply()
        }
    }

    fun getLanguage(): String {
        return sharedPreferences.getString(LANGUAGE_KEY, "System default") ?: "System default"
    }

    companion object {
        private const val LANGUAGE_KEY = "language"
    }
}