package alexschool.bookreader.network

import alexschool.bookreader.network.model.dto.BookInfo
import alexschool.bookreader.network.model.dto.Category
import alexschool.bookreader.network.model.dto.PostResponse
import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import javax.inject.Inject

class ApiServiceImpl @Inject constructor(private val httpClient: HttpClient) : ApiService {

    private suspend inline fun <reified T> safeApiCall(request: suspend () -> T): List<T> {
        return try {
            listOf(request())
        } catch (e: RedirectResponseException) {
            Log.d("ApiResponse", "3xx Error: ${e.response.status.description}")
            emptyList()
        } catch (e: ClientRequestException) {
            Log.d("ApiResponse", "4xx Error: ${e.response.status.description}")
            emptyList()
        } catch (e: ServerResponseException) {
            Log.d("ApiResponse", "5xx Error: ${e.response.status.description}")
            emptyList()
        } catch (e: Exception) {
            Log.d("ApiResponse", "Error: ${e.message}")
            emptyList()
        }
    }

    override suspend fun getPosts(): List<PostResponse>
    = safeApiCall {
        httpClient.get(Util.POSTS_URL).body()
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


    override suspend fun getBookInfo(): List<BookInfo> =
    safeApiCall {
        httpClient.get("${Util.BASE_URL}/${Util.BOOK_INFO_ENDPOINT}").body()
    }
//    {
//        return try {
//            httpClient.get("${Util.BASE_URL}/${Util.BOOK_INFO_ENDPOINT}").body()
//        } catch (e: RedirectResponseException) {
//            //3xx - responses
//            Log.d("ApiResponse", "3xx Error: ${e.response.status.description}")
//            emptyList()
//        } catch (e: ClientRequestException) {
//            //4xx - responses
//            Log.d("ApiResponse", "4xx Error: ${e.response.status.description}")
//            emptyList()
//        } catch (e: ServerResponseException) {
//            //5xx - responses
//            Log.d("ApiResponse", "5xx Error: ${e.response.status.description}")
//            emptyList()
//        } catch (e: Exception) {
//            Log.d("ApiResponse", "Error: ${e.message}")
//            emptyList()
//        }
//
//    }

    override suspend fun getCategories(): List<Category>
//    = safeApiCall {
//        httpClient.get(Util.CATEGORIES_URL).body()
//    }
    {
        return try {
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

}