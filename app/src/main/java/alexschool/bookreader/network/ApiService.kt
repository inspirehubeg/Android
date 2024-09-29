package alexschool.bookreader.network

import alexschool.bookreader.data.remote.BookInfoDto
import alexschool.bookreader.data.remote.CategoryDto
import alexschool.bookreader.domain.PostResponse


interface ApiService {
    suspend fun getBookInfo(): List<BookInfoDto>

    suspend fun getCategories(): List<CategoryDto>

    suspend fun getPosts(): List<PostResponse>
}