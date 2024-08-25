package ih.tools.readingpad.feature_note_color.data.repository

import ih.tools.readingpad.feature_note_color.data.data_source.ThemeColorDao
import ih.tools.readingpad.feature_note_color.domain.model.ThemeColor
import ih.tools.readingpad.feature_note_color.domain.model.ThemeColorType
import ih.tools.readingpad.feature_note_color.domain.repository.ThemeColorRepository

class ThemeColorRepositoryImpl(private val dao: ThemeColorDao) : ThemeColorRepository {
    override suspend fun getAll(type: ThemeColorType): List<ThemeColor> {
        return dao.getAll(type)
    }

    override suspend fun insert(themeColor: ThemeColor) {
        dao.insert(themeColor)
    }

    override suspend fun delete(themeColor: ThemeColor) {
        dao.delete(themeColor)
    }

    override suspend fun deleteAll(type: ThemeColorType) {
        dao.deleteAll(type)
    }

    override suspend fun colorExists(argb: Int): Int {
        return dao.colorExists(argb)
    }
}