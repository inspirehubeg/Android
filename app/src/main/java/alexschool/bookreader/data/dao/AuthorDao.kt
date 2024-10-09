package alexschool.bookreader.data.dao

import alexSchool.network.entities.AuthorEntity
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface AuthorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuthor(author: AuthorEntity)

    @Query("SELECT * FROM authors WHERE id = :authorId")
    suspend fun getAuthorById(authorId: Int): AuthorEntity?

    @Query("DELETE FROM authors WHERE id = :authorId")
    suspend fun deleteAuthorById(authorId: Int)

    @Query("SELECT * FROM authors")
    suspend fun getAllAuthors(): List<AuthorEntity>

}
