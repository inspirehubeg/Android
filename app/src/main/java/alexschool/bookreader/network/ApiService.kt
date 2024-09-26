package alexschool.bookreader.network

import alexschool.bookreader.network.model.dto.BookInfo
import alexschool.bookreader.network.model.dto.Category
import alexschool.bookreader.network.model.dto.PostResponse


interface ApiService {
    suspend fun getBookInfo(): List<BookInfo>

    suspend fun getCategories(): List<Category>

    suspend fun getPosts(): List<PostResponse>
}