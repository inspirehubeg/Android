package alexschool.bookreader.data.dao

import alexschool.bookreader.data.local.BookEntity
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: BookEntity)

    @Query("SELECT * FROM books WHERE id = :bookId")
    suspend fun getBookById(bookId: Int): BookEntity?

    @Query("DELETE FROM books WHERE id = :bookId")
    suspend fun deleteBookById(bookId: Int)

    @Query("SELECT * FROM books")
    suspend fun getAllBooks(): List<BookEntity>
}