package ih.tools.readingpad.remote

import alexSchool.network.NetworkModule
import alexSchool.network.data.Token
import alexSchool.network.entities.MetadataEntity
import android.util.Log
import ih.tools.readingpad.feature_book_parsing.data.data_source.ReadingPadDatabase
import ih.tools.readingpad.mappers.toMetadataEntity
import ih.tools.readingpad.mappers.toToken
import ih.tools.readingpad.mappers.toTokenEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class BookContentRepoImpl(private val readingPadDatabase: ReadingPadDatabase) :
    BookContentRepository {
    private val defaultDispatcher = NetworkModule.provideDispatcher()
    private val apiService = NetworkModule.provideApiService()

    override suspend fun getMetadata(bookId: Int): Flow<MetadataEntity> = flow {
        val localMetadata = withContext(defaultDispatcher) {
            readingPadDatabase.bookContentDao.getMetadataByBookId(bookId)
        }
        if (localMetadata != null) {
            emit(localMetadata)
        }
        try {
            val dtoMetadata = withContext(defaultDispatcher) {
                apiService.getMetadata(bookId)
            }
            readingPadDatabase.bookContentDao.insertMetadata(dtoMetadata.toMetadataEntity())
            emit(dtoMetadata.toMetadataEntity())
        } catch (e: Exception) {
            emit(MetadataEntity(bookId, "", "", ""))
        }
    }


    override suspend fun getTokens(bookId: Int, tokenNum: Int): Flow<List<Token>> = flow {

        Log.d("PagesScreen", "getTokens is called")
        val localTokensForBook = withContext(defaultDispatcher) {
            readingPadDatabase.bookContentDao.getAllTokensForBook(bookId)
        }
        Log.d("PagesScreen", "localTokensForBook = ${localTokensForBook.first().count}")
        emit(localTokensForBook.map { it.toToken() })
        try {
            val dtoTokensForBook = withContext(defaultDispatcher)
            {
                Log.d("PagesScreen", "getToken from server is called")
                apiService.getTokens(bookId, tokenNum)
            }
            val updatedTokensForBook = dtoTokensForBook.map { dtoToken ->
                val localToken =
                    readingPadDatabase.bookContentDao.getTokenById(
                        bookId = bookId,
                        tokenId = dtoToken.id
                    )
                if (dtoToken.is_deleted == true) {
                    if (localToken != null) {
                        readingPadDatabase.bookContentDao
                            .deleteTokenById(bookId = bookId, tokenId = dtoToken.id)
                    }
                } else {
                    readingPadDatabase.bookContentDao.insertToken(dtoToken.toTokenEntity())
                }
                dtoToken.toTokenEntity().toToken()
            }
            emit(updatedTokensForBook)
        } catch (e: Exception) {
            emit(emptyList())
        }

    }
}