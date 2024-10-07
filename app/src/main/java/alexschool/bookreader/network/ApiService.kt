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

    suspend fun getTokens(bookId: Int) : List<TokenDto>

    suspend fun getSubscriptions() : List<SubscriptionDto>

}

