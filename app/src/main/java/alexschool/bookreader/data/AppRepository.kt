package alexschool.bookreader.data

import alexschool.bookreader.data.domain.Author
import alexschool.bookreader.data.domain.BookInfo
import alexschool.bookreader.data.domain.Category
import alexschool.bookreader.data.domain.DetailedBookInfo
import alexschool.bookreader.data.domain.GeneralBookInfo
import alexschool.bookreader.data.domain.ReadingProgress
import alexschool.bookreader.data.domain.SavedBook
import alexschool.bookreader.data.domain.Set
import alexschool.bookreader.data.domain.SetContent
import alexschool.bookreader.data.domain.Subscription
import alexschool.bookreader.data.domain.Tag
import alexschool.bookreader.data.domain.Translator
import kotlinx.coroutines.flow.Flow

interface AppRepository {

    suspend fun getCategories(): Flow<List<Category>>
    //suspend fun getBooksInfo(): Flow<List<BookDto>>
    suspend fun getRemoteBooks(): Flow<List<BookInfo>>
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







   // suspend fun getGeneralBookInfo(bookId: Int): GeneralBookInfo

}