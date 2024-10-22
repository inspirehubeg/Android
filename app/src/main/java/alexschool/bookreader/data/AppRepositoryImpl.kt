package alexschool.bookreader.data

import alexSchool.network.NetworkModule
import alexSchool.network.data.AlexSchoolDatabase
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
import alexSchool.network.mappers.toAuthor
import alexSchool.network.mappers.toAuthorEntity
import alexSchool.network.mappers.toBookInfo
import alexSchool.network.mappers.toBookInfoEntity
import alexSchool.network.mappers.toCategory
import alexSchool.network.mappers.toCategoryEntity
import alexSchool.network.mappers.toDetailedBookInfo
import alexSchool.network.mappers.toGeneralBookInfo
import alexSchool.network.mappers.toReadingProgress
import alexSchool.network.mappers.toReadingProgressEntity
import alexSchool.network.mappers.toSavedBook
import alexSchool.network.mappers.toSavedBookEntity
import alexSchool.network.mappers.toSet
import alexSchool.network.mappers.toSetEntity
import alexSchool.network.mappers.toSubscription
import alexSchool.network.mappers.toSubscriptionEntity
import alexSchool.network.mappers.toTag
import alexSchool.network.mappers.toTagEntity
import alexSchool.network.mappers.toTranslator
import alexSchool.network.mappers.toTranslatorEntity
import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val appDatabase: AlexSchoolDatabase,
) : AppRepository {

    private val defaultDispatcher: CoroutineDispatcher = NetworkModule.provideDispatcher()
    private val apiService = NetworkModule.provideApiService()


    override suspend fun getCategories(): Flow<List<Category>> = channelFlow {
        val localCategories = appDatabase.categoryDao().getAllCategories()
        // Emit cached categories first
        send(localCategories.map { it.toCategory() })
        // Fetch categories from API
        coroutineScope {
            launch(defaultDispatcher) {
                try {
                    val dtoCategories = withContext(defaultDispatcher) {
                        apiService.getCategories()
                    }
                    val updatedCategories = dtoCategories.map { dtoCategory ->
                        val localCategory =
                            appDatabase.categoryDao().getCategoryById(dtoCategory.id)
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
                    send(updatedCategories)

                } catch (e: Exception) {
                    // Handle API errors
                    send(emptyList()) // Or emit an error state
                }
            }
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

    override suspend fun getRemoteBooks(): Flow<List<BookInfo>> = channelFlow {
        val localBooks = appDatabase.bookDao().getAllBooks()
        send(localBooks.map { it.toBookInfo() })
        coroutineScope {
            launch(defaultDispatcher) {
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
                    send(remoteBooks)
                } catch (e: Exception) {
                    Log.d("ApiResponse", "Error: ${e.message}")
                }
            }
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

