package alexschool.bookreader.data.dao

import alexSchool.network.entities.TranslatorEntity
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TranslatorDao {
    @Insert(entity = TranslatorEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTranslator(translator: TranslatorEntity)

    @Query("SELECT * FROM translators")
    suspend fun getAllTranslators(): List<TranslatorEntity>

    @Query("SELECT * FROM translators WHERE id = :translatorId")
    suspend fun getTranslatorById(translatorId: Int): TranslatorEntity?

    @Query("DELETE FROM translators WHERE id = :translatorId")
    suspend fun deleteTranslatorById(translatorId: Int)

}