package alexschool.bookreader.network

import alexschool.bookreader.data.remote.AuthorDto
import alexschool.bookreader.data.remote.BookDto
import alexschool.bookreader.data.remote.CategoryDto
import alexschool.bookreader.data.remote.ReadingProgressDto
import alexschool.bookreader.data.remote.SavedBookDto
import alexschool.bookreader.data.remote.SetContentDto
import alexschool.bookreader.data.remote.SetDto
import alexschool.bookreader.data.remote.SubscriptionDto
import alexschool.bookreader.data.remote.TagDto
import alexschool.bookreader.data.remote.TokenDto
import alexschool.bookreader.data.remote.TranslatorDto
import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import javax.inject.Inject

class ApiServiceImpl @Inject constructor(private val httpClient: HttpClient) : ApiService {

    override suspend fun getGeneralChanges(): List<String> {
        val changes = mutableListOf<String>()
        return changes
    }

    override suspend fun getSavesChanges(): List<String> {
        val changes = mutableListOf<String>()
        return changes
    }

    override suspend fun getInputChanges(): List<String> {
        val changes = mutableListOf<String>()
        return changes
    }

//    {
//        return try {
//            httpClient.get(Util.POSTS_URL).body()
//        } catch (e: RedirectResponseException) {
//            //3xx - responses
//            Log.d("ApiResponse", "Error: ${e.response.status.description}")
//            emptyList()
//        } catch (e: ClientRequestException) {
//            //4xx - responses
//            Log.d("ApiResponse", "Error: ${e.response.status.description}")
//            emptyList()
//        } catch (e: ServerResponseException) {
//            //5xx - responses
//            Log.d("ApiResponse", "Error: ${e.response.status.description}")
//            emptyList()
//        } catch (e: Exception) {
//            Log.d("ApiResponse", "Error: ${e.message}")
//            emptyList()
//        }
//    }


    override suspend fun getBooks(): List<BookDto>
    {
        return try {
            Log.d("ApiResponse", "bookInfo Api called: ${Util.BOOK_INFO_URL}")
            httpClient.get(Util.BOOK_INFO_URL).body()

        } catch (e: RedirectResponseException) {
            //3xx - responses
            Log.d("ApiResponse", "3xx Error: ${e.response.status.description}")
            emptyList()
        } catch (e: ClientRequestException) {
            //4xx - responses
            Log.d("ApiResponse", "4xx Error: ${e.response.status.description}")
            emptyList()
        } catch (e: ServerResponseException) {
            //5xx - responses
            Log.d("ApiResponse", "5xx Error: ${e.response.status.description}")
            emptyList()
        } catch (e: Exception) {
            Log.d("ApiResponse", "Error: ${e.message}")
            emptyList()
        }

    }

    override suspend fun getCategories(): List<CategoryDto> {
        return try {
            Log.d("ApiResponse", "Categories Api called: ${Util.CATEGORIES_URL}")
            httpClient.get(Util.CATEGORIES_URL).body()
        } catch (e: RedirectResponseException) {
            //3xx - responses
            Log.d("ApiResponse", "3xx Error: ${e.response.status.description}")
            emptyList()
        } catch (e: ClientRequestException) {
            //4xx - responses
            Log.d("ApiResponse", "4xx Error: ${e.response.status.description}")
            emptyList()
        } catch (e: ServerResponseException) {
            //5xx - responses
            Log.d("ApiResponse", "5xx Error: ${e.response.status.description}")
            emptyList()
        } catch (e: Exception) {
            Log.d("ApiResponse", "Error: ${e.message}")
            emptyList()
        }
    }

    override suspend fun getTags(): List<TagDto> {
        TODO("Not yet implemented")
    }

    override suspend fun getAuthors(): List<AuthorDto> {
        TODO("Not yet implemented")
    }

    override suspend fun getTranslators(): List<TranslatorDto> {
        TODO("Not yet implemented")
    }

    override suspend fun getSetsByUserId(userId: Int): List<SetDto> {
        TODO("Not yet implemented")
    }


    override suspend fun getSetContent(): List<SetContentDto> {
        TODO("Not yet implemented")
    }

    override suspend fun getReadingProgressByUserId(userId: Int): List<ReadingProgressDto> {
        TODO("Not yet implemented")
    }

    override suspend fun getSavedBooksByUserId(userId: Int): List<SavedBookDto> {
        TODO("Not yet implemented")
    }

    override suspend fun getTokens(bookId: Int): List<TokenDto> {
        return try {
            httpClient.get(Util.TOKEN_URL).body()
        } catch (e: RedirectResponseException) {
            //3xx - responses
            Log.d("ApiResponse", "3xx Error: ${e.response.status.description}")
            emptyList()
        } catch (e: ClientRequestException) {
            //4xx - responses
            Log.d("ApiResponse", "4xx Error: ${e.response.status.description}")
            emptyList()
        } catch (e: ServerResponseException) {
            //5xx - responses
            Log.d("ApiResponse", "5xx Error: ${e.response.status.description}")
            emptyList()
        } catch (e: Exception) {
            Log.d("ApiResponse", "Error: ${e.message}")
            emptyList()
        }
    }


    override suspend fun getSubscriptions(): List<SubscriptionDto> {
        TODO("Not yet implemented")
    }

}