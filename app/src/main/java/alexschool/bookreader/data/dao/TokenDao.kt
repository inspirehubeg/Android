package alexschool.bookreader.data.dao

import alexschool.bookreader.data.remote.TokenDto
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface TokenDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToken(tokenDao: TokenDto)

    @Query("SELECT * FROM tokens WHERE id = :tokenId AND bookId = :bookId")
    suspend fun getTokenById(tokenId: Int, bookId: Int): TokenDto?

    @Query("DELETE FROM tokens WHERE id = :tokenId AND bookId = :bookId")
    suspend fun deleteTokenById(tokenId: Int, bookId: Int)

    @Query("SELECT * FROM tokens WHERE bookId = :bookId")
    suspend fun getAllTokensForBook(bookId: Int): List<TokenDto>

    @Query("DELETE FROM tokens WHERE bookId = :bookId")
    suspend fun deleteAllTokensForBook(bookId: Int)

}