package alexschool.bookreader.data

import alexSchool.network.domain.Author
import alexSchool.network.domain.BookInfo
import alexSchool.network.domain.Category
import alexSchool.network.domain.DetailedBookInfo
import alexSchool.network.domain.GeneralBookInfo
import alexSchool.network.domain.ReadingProgress
import alexSchool.network.domain.SavedBook
import alexSchool.network.domain.Set
import alexSchool.network.domain.SetContent
import alexSchool.network.domain.Subscription
import alexSchool.network.domain.Tag
import alexSchool.network.domain.Translator

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