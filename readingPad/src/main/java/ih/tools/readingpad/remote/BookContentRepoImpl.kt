package ih.tools.readingpad.remote

import alexSchool.network.NetworkModule
import alexSchool.network.data.AlexSchoolDatabase
import alexSchool.network.domain.DetailedBookInfo
import alexSchool.network.domain.Metadata
import alexSchool.network.domain.Token
import alexSchool.network.entities.Converters
import alexSchool.network.mappers.toDetailedBookInfo
import android.util.Log
import ih.tools.readingpad.feature_book_parsing.data.data_source.ReadingPadDatabase
import ih.tools.readingpad.mappers.toMetadata
import ih.tools.readingpad.mappers.toMetadataEntity
import ih.tools.readingpad.mappers.toToken
import ih.tools.readingpad.mappers.toTokenEntity
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BookContentRepoImpl @Inject constructor(
    private val readingPadDatabase: ReadingPadDatabase,
    private val alexSchoolDatabase: AlexSchoolDatabase,
    private val converters: Converters
) :
    BookContentRepository {
    private val defaultDispatcher = NetworkModule.provideDispatcher()
    private val apiService = NetworkModule.provideApiService()

    override suspend fun getMetadata(bookId: Int): Flow<Metadata> = channelFlow {
        Log.d("GetMetadata", "Starting metadata flow for bookId: $bookId") // Log flow start
        val localMetadata = withContext(defaultDispatcher) {
            alexSchoolDatabase.bookDao().getMetadataByBookId(bookId)
        }
        Log.d("GetMetadata", "Local metadata: $localMetadata") // Log local metadata
        localMetadata?.let {
            Log.d("GetMetadata", "Emitting local metadata") // Log local metadata emission
            send(it.toMetadata())
        } // Emit local metadata if available

        coroutineScope {
            launch(defaultDispatcher) {
                try {
                    val dtoMetadata = withContext(defaultDispatcher) {
                        apiService.getMetadata(bookId)
                    } ?: return@launch // Early return if remote metadata is null
                    Log.d("PagesScreen", "dtoMetadata is not null")
                    val metadataEntity = dtoMetadata.toMetadataEntity(converters)
                        ?: throw IllegalStateException("Failed to convert metadata") // Throw exception if conversion fails

                    Log.d("PagesScreen", "metadataEntity is not null")
                    alexSchoolDatabase.bookDao().insertMetadata(metadataEntity)

                    send(metadataEntity.toMetadata()) // Emit the final, synchronized metadata
                } catch (e: Exception) {
                    Log.e("GetMetadata", "Error fetching metadata: ${e.message}")
                    //send(Metadata(bookId, "", "", ""))
                }
            }
        }
    }


    override suspend fun getTokens(bookId: Int, tokenNum: Int): Flow<Token> = channelFlow {

        Log.d("PagesScreen", "getTokens is called book id = $bookId token num = $tokenNum")
        val localTokensForBook = withContext(defaultDispatcher) {
            alexSchoolDatabase.bookDao().getTokenById(bookId = bookId, tokenId = tokenNum)
        }
        Log.d("PagesScreen", "localTokensForBook = ${localTokensForBook}")
        localTokensForBook?.let {
            send(it.toToken())
        }
        try {
            val dtoTokensForBook = withContext(defaultDispatcher)
            {
                Log.d("PagesScreen", "getToken from server is called")
                val response = apiService.getTokens(bookId, tokenNum)
                Log.d("PagesScreen", "response = $response")
                response
            }
            Log.d("PagesScreen", "getToken after server")
            val localToken =
                alexSchoolDatabase.bookDao().getTokenById(
                    bookId = bookId,
                    tokenId = dtoTokensForBook!!.id
                )
            if (dtoTokensForBook.is_deleted == true) {
                if (localToken != null) {
                    alexSchoolDatabase.bookDao()
                        .deleteTokenById(bookId = bookId, tokenId = dtoTokensForBook.id)
                }
            } else {
                alexSchoolDatabase.bookDao().insertToken(dtoTokensForBook.toTokenEntity())
            }
            send(dtoTokensForBook.toTokenEntity().toToken())

        } catch (e: Exception) {
            Log.e("pagesScreen", "Error fetching tokens: ${e.message}")
        }

    }

    override suspend fun getBookInfo(bookId: Int): Flow<DetailedBookInfo> = channelFlow {
        val localBookInfo = withContext(defaultDispatcher) {
            alexSchoolDatabase.bookDao().getBookById(bookId)
        }
        val bookWithDetails = alexSchoolDatabase.bookDao().getBookWithDetails(bookId)
        if (localBookInfo != null) {
            send(localBookInfo.toDetailedBookInfo(bookWithDetails!!))
        }
    }
}