package alexschool.bookreader.data

import alexschool.bookreader.domain.BookInfo
import alexschool.bookreader.domain.Category
import alexschool.bookreader.domain.Token
import kotlinx.coroutines.flow.Flow

interface AppRepository {
    suspend fun getCategories(): Flow<List<Category>>
    suspend fun getBookInfo(): Flow<List<BookInfo>>
    suspend fun getToken() : Flow<Token>
}