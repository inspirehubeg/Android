package ih.tools.readingpad.feature_note_color.domain.repository

import ih.tools.readingpad.feature_note_color.domain.model.ThemeColor
import ih.tools.readingpad.feature_note_color.domain.model.ThemeColorType

interface ThemeColorRepository {
    suspend fun getAll(type: ThemeColorType): List<ThemeColor>
    suspend fun insert(themeColor: ThemeColor)
    suspend fun delete(themeColor: ThemeColor)
    suspend fun deleteAll(type: ThemeColorType)
    suspend fun colorExists(argb: Int): Int
}