package ih.tools.readingpad.remote

import alexSchool.network.entities.MetadataEntity
import alexSchool.network.entities.TokenEntity
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BookContentDao {

    @Query("SELECT * FROM metadata WHERE bookId = :bookId")
    suspend fun getMetadataByBookId(bookId: Int): MetadataEntity?
    @Insert(entity = MetadataEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMetadata(metadata: MetadataEntity)

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