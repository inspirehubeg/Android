package alexschool.bookreader.data.dao

import alexschool.bookreader.data.local.BookEntity
import alexschool.bookreader.data.local.BookWithDetails
import alexschool.bookreader.data.local.ReadingProgressEntity
import alexschool.bookreader.data.local.SavedBookEntity
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface BookDao {

    @Insert(entity = BookEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: BookEntity)

    @Query("SELECT * FROM books WHERE id = :bookId")
    suspend fun getBookById(bookId: Int): BookEntity?

    @Query("DELETE FROM books WHERE id = :bookId")
    suspend fun deleteBookById(bookId: Int)

    @Query("SELECT * FROM books")
    suspend fun getAllBooks(): List<BookEntity>

    @Transaction
    @Query("SELECT * FROM books WHERE id = :bookId")
    fun getBookWithDetails(bookId: Int): BookWithDetails?



    @Query("SELECT * FROM reading_progress WHERE userId = :userId")
    suspend fun getReadingProgressByUserId(userId: Int): List<ReadingProgressEntity>

    @Insert(entity = ReadingProgressEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReadingProgress(readingProgress: ReadingProgressEntity)

    @Query("SELECT * FROM reading_progress WHERE bookId = :bookId AND userId = :userId")
    suspend fun getReadingProgressByBookIdAndUserId(
        bookId: Int,
        userId: Int
    ): ReadingProgressEntity?



    @Query("SELECT * FROM saved_books WHERE userId = :userId")
    suspend fun getSavedBooksByUserId(userId: Int): List<SavedBookEntity>

    @Insert(entity = SavedBookEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedBook(savedBook: SavedBookEntity)

    @Query("DELETE FROM saved_books WHERE bookId = :bookId AND userId = :userId")
    suspend fun deleteSavedBook(bookId: Int, userId: Int)


}