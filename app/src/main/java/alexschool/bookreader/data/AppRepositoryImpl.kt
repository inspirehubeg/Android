package alexschool.bookreader.data

import alexSchool.network.NetworkModule
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
import alexschool.bookreader.data.local.AlexSchoolDatabase
import alexschool.bookreader.data.mappers.toAuthor
import alexschool.bookreader.data.mappers.toAuthorEntity
import alexschool.bookreader.data.mappers.toBookInfo
import alexschool.bookreader.data.mappers.toBookInfoEntity
import alexschool.bookreader.data.mappers.toCategory
import alexschool.bookreader.data.mappers.toCategoryEntity
import alexschool.bookreader.data.mappers.toDetailedBookInfo
import alexschool.bookreader.data.mappers.toGeneralBookInfo
import alexschool.bookreader.data.mappers.toReadingProgress
import alexschool.bookreader.data.mappers.toReadingProgressEntity
import alexschool.bookreader.data.mappers.toSavedBook
import alexschool.bookreader.data.mappers.toSavedBookEntity
import alexschool.bookreader.data.mappers.toSet
import alexschool.bookreader.data.mappers.toSetEntity
import alexschool.bookreader.data.mappers.toSubscription
import alexschool.bookreader.data.mappers.toSubscriptionEntity
import alexschool.bookreader.data.mappers.toTag
import alexschool.bookreader.data.mappers.toTagEntity
import alexschool.bookreader.data.mappers.toTranslator
import alexschool.bookreader.data.mappers.toTranslatorEntity
import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val appDatabase: AlexSchoolDatabase,
    //private val apiService: ApiService,
) : AppRepository {

    private val defaultDispatcher: CoroutineDispatcher = NetworkModule.provideDispatcher()
    private val apiService = NetworkModule.provideApiService()
    override suspend fun getCategories(): Flow<List<Category>> = flow {
        val localCategories = appDatabase.categoryDao().getAllCategories()
        // Emit cached categories first
        emit(localCategories.map { it.toCategory() })
        // Fetch categories from API
        try {
            val dtoCategories = withContext(defaultDispatcher) {
                apiService.getCategories()
            }
            val updatedCategories = dtoCategories.map { dtoCategory ->
                val localCategory = appDatabase.categoryDao().getCategoryById(dtoCategory.id)
                if (dtoCategory.is_deleted == true) {
                    if (localCategory != null) {
                        appDatabase.categoryDao().deleteCategoryById(dtoCategory.id)
                    }
                } else { // isDeleted either null or false
                    appDatabase.categoryDao().insertCategory(dtoCategory.toCategoryEntity())
                }
                dtoCategory.toCategoryEntity().toCategory()
            }
            // Emit updated categories
            emit(updatedCategories)

        } catch (e: Exception) {
            // Handle API errors
            emit(emptyList()) // Or emit an error state
        }
    }

    override suspend fun getTags(): Flow<List<Tag>> = flow {
        val localTags = appDatabase.tagDao().getAllTags()
        emit(localTags.map { it.toTag() })
        // Fetch tags from API
        try {
            val dtoTags = withContext(defaultDispatcher) {
                apiService.getTags()
            }
            val updatedTags = dtoTags.map { dtoTag ->
                val localTag = appDatabase.tagDao().getTagById(dtoTag.id)
                if (dtoTag.is_deleted == true) {
                    if (localTag != null) {
                        appDatabase.tagDao().deleteTagById(dtoTag.id)
                    }
                } else { // isDeleted either null or false
                    appDatabase.tagDao().insertTag(dtoTag.toTagEntity())
                }
                dtoTag.toTagEntity().toTag()
            }
            emit(updatedTags)

        } catch (e: Exception) {
            // Handle API errors
            emit(emptyList()) // Or emit an error state
        }
    }

    override suspend fun getAuthors(): Flow<List<Author>> = flow {
        val localAuthors = appDatabase.authorDao().getAllAuthors()
        emit(localAuthors.map { it.toAuthor() })
        try {
            val dtoAuthors = withContext(defaultDispatcher) {
                apiService.getAuthors()
            }
            val updatedAuthors = dtoAuthors.map { dtoAuthor ->
                val localAuthor = appDatabase.authorDao().getAuthorById(dtoAuthor.id)
                if (dtoAuthor.is_deleted == true) {
                    if (localAuthor != null) {
                        appDatabase.authorDao().deleteAuthorById(dtoAuthor.id)
                    }
                } else {
                    appDatabase.authorDao().insertAuthor(dtoAuthor.toAuthorEntity())
                }
                dtoAuthor.toAuthorEntity().toAuthor()
            }
            emit(updatedAuthors)

        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    override suspend fun getRemoteBooks(): Flow<List<BookInfo>> = flow {
        try {
            val dtoBooks = withContext(defaultDispatcher) {
                apiService.getBooks()
            }
            val remoteBooks = dtoBooks.map { bookDto ->
                val localBook = appDatabase.bookDao().getBookById(bookDto.book.id)
                if (bookDto.book.is_deleted == true) {
                    if (localBook != null) {
                        appDatabase.bookDao().deleteBookById(bookDto.book.id)
                    }
                } else {
                    // add bookInfo to its table in db
                    appDatabase.bookDao().insertBook(bookDto.toBookInfoEntity())
                }
                bookDto.toBookInfoEntity().toBookInfo()
                //then reload the ui with the new data?
            }
            emit(remoteBooks)
        } catch (e: Exception) {
            Log.d("ApiResponse", "Error: ${e.message}")
        }
    }

    override suspend fun getGeneralBooksInfo(): Flow<List<GeneralBookInfo>> {
        val localBooks = appDatabase.bookDao().getAllBooks()

        if (localBooks.isEmpty()) {
            return getRemoteBooks().map { remoteBooks ->
                remoteBooks.map { book ->
                    val bookWithDetails = appDatabase.bookDao().getBookWithDetails(book.id)
                    book.toGeneralBookInfo(bookWithDetails!!)
                }
            }

        } else {
            return flow {
                emit(localBooks.map { book ->
                    val bookWithDetails = appDatabase.bookDao().getBookWithDetails(book.id)
                    book.toBookInfo().toGeneralBookInfo(bookWithDetails!!)
                })
            }
        }
    }

    override suspend fun getDetailedBookInfo(bookId: Int): DetailedBookInfo? {
        val requiredBook = appDatabase.bookDao().getBookById(bookId)
        val bookWithDetails = appDatabase.bookDao().getBookWithDetails(bookId)
        if (requiredBook != null) {
            return requiredBook.toDetailedBookInfo(bookWithDetails!!)
        } else {
            println("Book with id $bookId not found")
            return null
        }
    }

    override suspend fun getTranslators(): Flow<List<Translator>> = flow {
        val localTranslators = appDatabase.translatorDao().getAllTranslators()
        emit(localTranslators.map { it.toTranslator() })
        try {
            val dtoTranslators = withContext(defaultDispatcher) {
                apiService.getTranslators()
            }
            val updatedTranslators = dtoTranslators.map { dtoTranslator ->
                val localTranslator =
                    appDatabase.translatorDao().getTranslatorById(dtoTranslator.id)
                if (dtoTranslator.is_deleted == true) {
                    if (localTranslator != null) {
                        appDatabase.translatorDao().deleteTranslatorById(dtoTranslator.id)
                    }
                } else {
                    appDatabase.translatorDao().insertTranslator(dtoTranslator.toTranslatorEntity())
                }
                dtoTranslator.toTranslatorEntity().toTranslator()
            }
            emit(updatedTranslators)

        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    override suspend fun getSubscriptions(): Flow<List<Subscription>> = flow {
        val localSubscriptions = appDatabase.subscriptionDao().getAllSubscriptions()
        emit(localSubscriptions.map { it.toSubscription() })
        try {
            val dtoSubscriptions = withContext(defaultDispatcher) {
                apiService.getSubscriptions()
            }
            val updatedSubscriptions = dtoSubscriptions.map { dtoSubscription ->
                val localSubscription =
                    appDatabase.subscriptionDao().getSubscriptionById(dtoSubscription.id)
                if (dtoSubscription.is_deleted == true) {
                    if (localSubscription != null) {
                        appDatabase.subscriptionDao().deleteSubscriptionById(dtoSubscription.id)
                    }
                } else {
                    appDatabase.subscriptionDao()
                        .insertSubscription(dtoSubscription.toSubscriptionEntity())
                }
                dtoSubscription.toSubscriptionEntity().toSubscription()
            }
            emit(updatedSubscriptions)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }


    override suspend fun getGeneralChanges(): List<String> {
        return apiService.getGeneralChanges()
    }

    override suspend fun getSavesChanges(userId: Int): List<String> {
        return apiService.getSavesChanges()
    }

    override suspend fun getInputChanges(userId: Int, bookId: Int): List<String> {
        return apiService.getInputChanges()
    }


    override suspend fun getSets(userId: Int): Flow<List<Set>> = flow {
        val localUserSets = appDatabase.setDao().getSetsByUserId(userId)
        emit(localUserSets.map { it.toSet() })
        try {
            val dtoSets = withContext(defaultDispatcher) {
                apiService.getSetsByUserId(userId)
            }
            val updatedSets = dtoSets.map { dtoSet ->
                val localSet = appDatabase.setDao().getSetById(dtoSet.id)
                if (dtoSet.is_deleted == true) {
                    if (localSet != null) {
                        appDatabase.setDao().deleteSetById(dtoSet.id)
                    }
                } else {
                    appDatabase.setDao().insertSet(dtoSet.toSetEntity())
                }
                dtoSet.toSetEntity().toSet()
            }
            emit(updatedSets)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    override suspend fun getSetContent(userId: Int, setId: Int): Flow<List<SetContent>> {
        TODO("Not yet implemented")
    }

    override suspend fun getReadingProgress(userId: Int): Flow<List<ReadingProgress>> = flow {
        val localUserProgress = appDatabase.bookDao().getReadingProgressByUserId(userId)
        emit(localUserProgress.map { it.toReadingProgress() })
        try {
            val dtoProgress = withContext(defaultDispatcher) {
                apiService.getReadingProgressByUserId(userId)
            }
            val updatedProgress = dtoProgress.map { progress ->
                appDatabase.bookDao().insertReadingProgress(progress.toReadingProgressEntity())
                progress.toReadingProgressEntity().toReadingProgress()
            }
            emit(updatedProgress)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    override suspend fun getSavedBooks(userId: Int): Flow<List<SavedBook>> = flow {
        val localUserSavedBooks = appDatabase.bookDao().getSavedBooksByUserId(userId)
        emit(localUserSavedBooks.map { it.toSavedBook() })

        try {
            val dtoSavedBooks = withContext(defaultDispatcher) {
                apiService.getSavedBooksByUserId(userId)
            }
            val updatedSavedBooks = dtoSavedBooks.map { dtoSavedBook ->
                appDatabase.bookDao().insertSavedBook(dtoSavedBook.toSavedBookEntity())
                dtoSavedBook.toSavedBookEntity().toSavedBook()
            }
            emit(updatedSavedBooks)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }




}

