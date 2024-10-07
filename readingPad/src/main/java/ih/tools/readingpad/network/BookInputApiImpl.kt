package ih.tools.readingpad.network

import ih.tools.readingpad.remote.BookmarkDto
import ih.tools.readingpad.remote.HighlightDto
import ih.tools.readingpad.remote.NoteDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject



class BookInputApiImpl @Inject constructor(private val httpClient: HttpClient) : BookInputApi {
    override suspend fun getHighlights(bookId: String): List<HighlightDto> {
        return httpClient.get("/books/$bookId/highlights").body()
    }

    override suspend fun addHighlight(highlightDto: HighlightDto) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteHighlight(highlightDto: HighlightDto) {
        TODO("Not yet implemented")
    }

    override suspend fun getBookmarks(bookId: String): List<BookmarkDto> {
        return httpClient.get("/books/$bookId/bookmarks").body()
    }

    override suspend fun addBookmark(bookmarkDto: BookmarkDto) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteBookmark(bookmarkDto: BookmarkDto) {
        TODO("Not yet implemented")
    }

    override suspend fun getNotes(bookId: String): List<NoteDto> {
        return httpClient.get("/books/$bookId/notes").body()
    }

    override suspend fun addNote(noteDto: NoteDto) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteNote(noteDto: NoteDto) {
        TODO("Not yet implemented")
    }


}