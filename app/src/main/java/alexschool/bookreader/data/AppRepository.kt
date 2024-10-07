package alexschool.bookreader.data

import alexschool.bookreader.data.local.BookEntity
import alexschool.bookreader.domain.Author
import alexschool.bookreader.domain.Category
import alexschool.bookreader.domain.DetailedBookInfo
import alexschool.bookreader.domain.GeneralBookInfo
import alexschool.bookreader.domain.ReadingProgress
import alexschool.bookreader.domain.SavedBook
import alexschool.bookreader.domain.Set
import alexschool.bookreader.domain.SetContent
import alexschool.bookreader.domain.Subscription
import alexschool.bookreader.domain.Tag
import alexschool.bookreader.domain.Token
import alexschool.bookreader.domain.Translator
import kotlinx.coroutines.flow.Flow

interface AppRepository {

    suspend fun getCategories(): Flow<List<Category>>
    //suspend fun getBooksInfo(): Flow<List<BookDto>>
    suspend fun getRemoteBooks(): Flow<List<BookEntity>>
    suspend fun getGeneralBooksInfo(): Flow<List<GeneralBookInfo>>
    suspend fun getDetailedBookInfo(bookId: Int): DetailedBookInfo?
    suspend fun getGeneralChanges() : List<String>
    suspend fun getTags() : Flow<List<Tag>>
    suspend fun getAuthors() : Flow<List<Author>>
    suspend fun getTranslators() : Flow<List<Translator>>
    suspend fun getSubscriptions() : Flow<List<Subscription>>

    suspend fun getSavesChanges(userId: Int) : List<String>
    suspend fun getInputChanges(userId: Int, bookId: Int) : List<String>

    suspend fun getSets(userId: Int) : Flow<List<Set>>
    suspend fun getSetContent(userId: Int, setId: Int) : Flow<List<SetContent>>
    suspend fun getReadingProgress(userId: Int) : Flow<List<ReadingProgress>>
    suspend fun getSavedBooks(userId: Int) : Flow<List<SavedBook>>


    suspend fun getTokens(bookId: Int) : Flow<List<Token>>




   // suspend fun getGeneralBookInfo(bookId: Int): GeneralBookInfo

}