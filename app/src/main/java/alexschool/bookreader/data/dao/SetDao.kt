package alexschool.bookreader.data.dao

import alexSchool.network.entities.SetEntity
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao

interface SetDao {
    @Insert(entity = SetEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSet(set: SetEntity)

    @Query("SELECT * FROM sets")
    suspend fun getAllSets(): List<SetEntity>

    @Query("SELECT * FROM sets WHERE id = :setId")
    suspend fun getSetById(setId: Int): SetEntity?

    @Query("DELETE FROM sets WHERE id = :setId")
    suspend fun deleteSetById(setId: Int)

    @Query("SELECT * FROM sets WHERE userId = :userId")
    suspend fun getSetsByUserId(userId: Int): List<SetEntity>

}