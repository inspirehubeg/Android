package ih.tools.readingpad.remote

import alexSchool.network.data.Token
import alexSchool.network.entities.MetadataEntity
import kotlinx.coroutines.flow.Flow

interface BookContentRepository {
    suspend fun getMetadata(bookId: Int) : Flow<MetadataEntity>

    suspend fun getTokens(bookId: Int, tokenNum: Int) : Flow<List<Token>>

}