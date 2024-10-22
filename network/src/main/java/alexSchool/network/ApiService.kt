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


interface ApiService {

    suspend fun getGeneralChanges() : List<String>
    suspend fun getSavesChanges() : List<String>
    suspend fun getInputChanges() : List<String>

    //this is called when there is changes in book table
    suspend fun getBooks(): List<BookDto>

    //this is called when there is changes in category table
    suspend fun getCategories(): List<CategoryDto>

    //this is called when there is changes in tag table
    suspend fun getTags() : List<TagDto>

    //this is called when there is changes in author table
    suspend fun getAuthors() : List<AuthorDto>

    //this is called when there is changes in translator table
    suspend fun getTranslators() : List<TranslatorDto>

    //this is called when there is changes in set table
    suspend fun getSetsByUserId(userId: Int) : List<SetDto>

    //this is called when there is changes in set_content table
    suspend fun getSetContent() : List<SetContentDto>

    //this is called when there is changes in reading_progress table
    suspend fun getReadingProgressByUserId(userId: Int) : List<ReadingProgressDto>

    //this is called when there is changes in saved_book table
    suspend fun getSavedBooksByUserId(userId: Int) : List<SavedBookDto>

    suspend fun getTokens(bookId: Int, tokenNum: Int) : TokenDto?
    suspend fun getMetadata(bookId: Int): MetadataDto?

    suspend fun getSubscriptions() : List<SubscriptionDto>




    suspend fun getHighlights(bookId: Int): List<HighlightDto>
    suspend fun addHighlight(highlightDto: HighlightDto)
    suspend fun deleteHighlight(highlightDto: HighlightDto)


    suspend fun getBookmarks(bookId: Int): List<BookmarkDto>
    suspend fun addBookmark(bookmarkDto: BookmarkDto)
    suspend fun deleteBookmark(bookmarkDto: BookmarkDto)


    suspend fun getNotes(bookId: Int): List<NoteDto>
    suspend fun addNote(noteDto: NoteDto)
    suspend fun deleteNote(noteDto: NoteDto)

}

