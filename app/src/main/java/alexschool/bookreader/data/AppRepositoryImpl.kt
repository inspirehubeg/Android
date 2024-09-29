package alexschool.bookreader.data

import alexschool.bookreader.data.local.AlexSchoolDatabase
import alexschool.bookreader.data.mappers.toAuthorEntity
import alexschool.bookreader.data.mappers.toBookEntity
import alexschool.bookreader.data.mappers.toBookInfo
import alexschool.bookreader.data.mappers.toBookInfoEntity
import alexschool.bookreader.data.mappers.toCategory
import alexschool.bookreader.data.mappers.toCategoryEntity
import alexschool.bookreader.domain.BookInfo
import alexschool.bookreader.domain.Category
import alexschool.bookreader.domain.Token
import alexschool.bookreader.network.ApiService
import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val appDatabase: AlexSchoolDatabase,
    private val apiService: ApiService,
    private val defaultDispatcher: CoroutineDispatcher
) : AppRepository {

    override suspend fun getCategories(): Flow<List<Category>> = flow {
        val localCategories = appDatabase.categoryDao().getAllCategories()
        // Emit cached categories first
        emit(localCategories.map { it.toCategory() })
        // Fetch categories from API
        try {
            val dtoCategories = withContext(defaultDispatcher) {
                apiService.getCategories()
            }

            // Update/insert categories based on timestamps
            for (dtoCategory in dtoCategories) {
                val localCategory = appDatabase.categoryDao().getCategoryById(dtoCategory.id)
                if (localCategory == null || dtoCategory.updated_at > localCategory.updatedAt) {
                    appDatabase.categoryDao().insertCategory(dtoCategory.toCategoryEntity())
                }
            }

            // Handle deletions
            val remoteCategoryIds = dtoCategories.map { it.id }
            val localCategoryIds = appDatabase.categoryDao().getCategoryIds()
            val categoriesToDelete = localCategoryIds.filter { it !in remoteCategoryIds }
            if (categoriesToDelete.isNotEmpty()) {
                appDatabase.categoryDao().deleteCategoriesByIds(categoriesToDelete)
            }
            if (dtoCategories == localCategories) return@flow
            else
            // Emit updated categories from database
                emit(appDatabase.categoryDao().getAllCategories().map { it.toCategory() })
        } catch (e: Exception) {
            // Handle API errors
            emit(emptyList()) // Or emit an error state
        }
    }

    override suspend fun getBookInfo(): Flow<List<BookInfo>> = flow {
        try {
            val dtoBookInfo = withContext(defaultDispatcher) {
                apiService.getBookInfo()
            }
            Log.d("ApiResponse", "BookInfo Api called: ${dtoBookInfo[0].book.id}")
            dtoBookInfo.forEach { bookInfo ->
                appDatabase.bookInfoDao().insertBookInfo(bookInfo.toBookInfoEntity())
                appDatabase.authorDao().insertAuthor(bookInfo.author.toAuthorEntity())
                appDatabase.bookDao().insertBook(bookInfo.book.toBookEntity())
            }
            emit(appDatabase.bookInfoDao().getAllBookInfo().map { it.toBookInfo() })
        } catch (e: Exception) {
            Log.d("ApiResponse", "Error: ${e.message}")
            emit(emptyList())
        }
    }

    override suspend fun getToken(): Flow<Token> {
        TODO("Not yet implemented")
    }
}