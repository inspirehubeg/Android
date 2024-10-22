package alexSchool.network

import alexSchool.network.dtos.AuthorDto
import alexSchool.network.dtos.BookDto
import alexSchool.network.dtos.BookmarkDto
import alexSchool.network.dtos.CategoryDto
import alexSchool.network.dtos.HighlightDto
import alexSchool.network.dtos.MetadataDto
import alexSchool.network.dtos.NoteDto
import alexSchool.network.dtos.ReadingProgressDto
import alexSchool.network.dtos.SavedBookDto
import alexSchool.network.dtos.SetContentDto
import alexSchool.network.dtos.SetDto
import alexSchool.network.dtos.SubscriptionDto
import alexSchool.network.dtos.TagDto
import alexSchool.network.dtos.TokenDto
import alexSchool.network.dtos.TranslatorDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get

class ApiServiceImpl(private val httpClient: HttpClient) : ApiService {

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


    override suspend fun getBooks(): List<BookDto> {
        return try {
            httpClient.get(Util.BOOK_INFO_URL).body()

        } catch (e: RedirectResponseException) {
            //3xx - responses
            println("ApiResponse: 3xx Error: ${e.response.status.description}")
            emptyList()
        } catch (e: ClientRequestException) {
            //4xx - responses
            println("ApiResponse, 4xx Error: ${e.response.status.description}")
            emptyList()
        } catch (e: ServerResponseException) {
            //5xx - responses
            println("ApiResponse: 5xx Error: ${e.response.status.description}")
            emptyList()
        } catch (e: Exception) {
            println("ApiResponse:Book Error: ${e.message}")
            emptyList()
        }

    }

    override suspend fun getCategories(): List<CategoryDto> {
        return try {
            // Log.d("ApiResponse", "Categories Api called: ${Util.CATEGORIES_URL}")
            httpClient.get(Util.CATEGORIES_URL).body()
        } catch (e: RedirectResponseException) {
            //3xx - responses
            println("ApiResponse: 3xx Error: ${e.response.status.description}")
            emptyList()
        } catch (e: ClientRequestException) {
            //4xx - responses
            println("ApiResponse, 4xx Error: ${e.response.status.description}")
            emptyList()
        } catch (e: ServerResponseException) {
            //5xx - responses
            println("ApiResponse: 5xx Error: ${e.response.status.description}")
            emptyList()
        } catch (e: Exception) {
            println("ApiResponse:Category Error: ${e.message}")
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

    override suspend fun getTokens(bookId: Int, tokenNum: Int): TokenDto? {
        return try {
             httpClient.get("${Util.BASE_URL}${Util.BOOK_INFO_ENDPOINT}${bookId}${Util.TOKEN_ENDPOINT}${tokenNum}").body()
            //httpClient.get("http://192.168.1.47:8080/api/v1/books/25/tokens/1").body()
        } catch (e: RedirectResponseException) {
            //3xx - responses
            println("ApiResponse: 3xx Error: ${e.response.status.description}")
            // emptyList()
            null
        } catch (e: ClientRequestException) {
            //4xx - responses
            println("ApiResponse, 4xx Error: ${e.response.status.description}")
            //emptyList()
            null
        } catch (e: ServerResponseException) {
            //5xx - responses
            println("ApiResponse: 5xx Error: ${e.response.status.description}")
            //emptyList()
            null
        } catch (e: Exception) {
            println("ApiResponse:Token Error: ${e.message}")
            // emptyList()
            null
        }
    }

    override suspend fun getMetadata(bookId: Int): MetadataDto? {
        return try {
            httpClient.get(
                "http://192.168.1.47:8080/api/v1/books/$bookId/metadata"
                //"${Util.BASE_URL}${Util.BOOK_INFO_ENDPOINT}${bookId}${Util.METADATA_FOR_BOOK_ENDPOINT}"
            )
                .body()
        } catch (e: RedirectResponseException) {
            //3xx - responses
            println("ApiResponse: Metadata 3xx Error: ${e.response.status.description}")
            //MetadataDto(bookId, "", "", "")
            null
        } catch (e: ClientRequestException) {
            //4xx - responses
            println("ApiResponse: Metadata 4xx Error: ${e.response.status.description}")
            null
            //MetadataDto(bookId, "", "", "")
        } catch (e: ServerResponseException) {
            //5xx - responses
            println("ApiResponse: Metadata 5xx Error: ${e.response.status.description}")
            null
            //MetadataDto(bookId, "", "", "")
        } catch (e: Exception) {
            println("ApiResponse: Metadata Error: ${e.message}")
            null
            // MetadataDto(bookId, "", "", "")
        }
    }


    override suspend fun getSubscriptions(): List<SubscriptionDto> {
        TODO("Not yet implemented")
    }


    override suspend fun getHighlights(bookId: Int): List<HighlightDto> {
        return httpClient.get("/books/$bookId/highlights").body()
    }

    override suspend fun addHighlight(highlightDto: HighlightDto) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteHighlight(highlightDto: HighlightDto) {
        TODO("Not yet implemented")
    }

    override suspend fun getBookmarks(bookId: Int): List<BookmarkDto> {
        return httpClient.get("/books/$bookId/bookmarks").body()
    }

    override suspend fun addBookmark(bookmarkDto: BookmarkDto) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteBookmark(bookmarkDto: BookmarkDto) {
        TODO("Not yet implemented")
    }

    override suspend fun getNotes(bookId: Int): List<NoteDto> {
        return httpClient.get("/books/$bookId/notes").body()
    }

    override suspend fun addNote(noteDto: NoteDto) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteNote(noteDto: NoteDto) {
        TODO("Not yet implemented")
    }


}