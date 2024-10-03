package alexschool.bookreader.data.dao

import alexschool.bookreader.data.local.TokenEntity
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface TokenDao {
    @Insert(entity = TokenEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToken(tokenEntity: TokenEntity)

    @Query("SELECT * FROM tokens WHERE id = :tokenId AND bookId = :bookId")
    suspend fun getTokenById(tokenId: Int, bookId: Int): TokenEntity?

    @Query("DELETE FROM tokens WHERE id = :tokenId AND bookId = :bookId")
    suspend fun deleteTokenById(tokenId: Int, bookId: Int)

    @Query("SELECT * FROM tokens WHERE bookId = :bookId")
    suspend fun getAllTokensForBook(bookId: Int): List<TokenEntity>

    @Query("DELETE FROM tokens WHERE bookId = :bookId")
    suspend fun deleteAllTokensForBook(bookId: Int)

}