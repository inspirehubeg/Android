package alexschool.bookreader.data.dao

import alexschool.bookreader.data.local.BookInfoEntity
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BookInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookInfo(bookInfo: BookInfoEntity)

    @Query("SELECT * FROM book_info WHERE bookId = :bookId")
    suspend fun getBookInfoByBookId(bookId: Int): BookInfoEntity?

    @Query("DELETE FROM book_info WHERE bookId = :bookId")
    suspend fun deleteBookInfoByBookId(bookId: Int)

    @Query("SELECT * FROM book_info")
    suspend fun getAllBookInfo(): List<BookInfoEntity>
}