package ih.tools.readingpad.network

import ih.tools.readingpad.remote.BookmarkDto
import ih.tools.readingpad.remote.HighlightDto
import ih.tools.readingpad.remote.NoteDto

interface BookInputApi {

    suspend fun getHighlights(bookId: String): List<HighlightDto>
    suspend fun addHighlight(highlightDto: HighlightDto)
    suspend fun deleteHighlight(highlightDto: HighlightDto)


    suspend fun getBookmarks(bookId: String): List<BookmarkDto>
    suspend fun addBookmark(bookmarkDto: BookmarkDto)
    suspend fun deleteBookmark(bookmarkDto: BookmarkDto)


    suspend fun getNotes(bookId: String): List<NoteDto>
    suspend fun addNote(noteDto: NoteDto)
    suspend fun deleteNote(noteDto: NoteDto)
}