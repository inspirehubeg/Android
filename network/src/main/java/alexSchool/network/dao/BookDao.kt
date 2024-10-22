package alexSchool.network.dao


import alexSchool.network.entities.BookInfoEntity
import alexSchool.network.entities.MetadataEntity
import alexSchool.network.entities.ReadingProgressEntity
import alexSchool.network.entities.SavedBookEntity
import alexSchool.network.entities.TokenEntity
import alexSchool.network.entities.cross_tables.BookWithDetails
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface BookDao {

    @Insert(entity = TokenEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToken(tokenEntity: TokenEntity)

    @Query("SELECT * FROM tokens WHERE count = :tokenId AND bookId = :bookId")
    suspend fun getTokenById(tokenId: Int, bookId: Int): TokenEntity?

    @Query("DELETE FROM tokens WHERE count = :tokenId AND bookId = :bookId")
    suspend fun deleteTokenById(tokenId: Int, bookId: Int)

    @Query("SELECT * FROM tokens WHERE bookId = :bookId")
    suspend fun getAllTokensForBook(bookId: Int): List<TokenEntity>

    @Query("DELETE FROM tokens WHERE bookId = :bookId")
    suspend fun deleteAllTokensForBook(bookId: Int)


    @Query("SELECT * FROM metadata WHERE bookId = :bookId")
    suspend fun getMetadataByBookId(bookId: Int): MetadataEntity?

    @Insert(entity = MetadataEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMetadata(metadata: MetadataEntity)


    @Insert(entity = BookInfoEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: BookInfoEntity)

    @Query("SELECT * FROM bookinfo WHERE id = :bookId")
    suspend fun getBookById(bookId: Int): BookInfoEntity?

    @Query("DELETE FROM bookInfo WHERE id = :bookId")
    suspend fun deleteBookById(bookId: Int)

    @Query("SELECT * FROM bookInfo")
    suspend fun getAllBooks(): List<BookInfoEntity>

    @Transaction
    @Query("SELECT * FROM bookInfo WHERE id = :bookId")
   suspend fun getBookWithDetails(bookId: Int): BookWithDetails?



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