package ih.tools.readingpad.remote

import alexSchool.network.domain.DetailedBookInfo
import alexSchool.network.domain.Metadata
import alexSchool.network.domain.Token
import kotlinx.coroutines.flow.Flow

interface BookContentRepository {
    suspend fun getMetadata(bookId: Int) : Flow<Metadata>

    suspend fun getTokens(bookId: Int, tokenNum: Int) : Flow<Token>

    suspend fun getBookInfo(bookId: Int) : Flow<DetailedBookInfo>
}