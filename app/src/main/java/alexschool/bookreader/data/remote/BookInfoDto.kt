package alexschool.bookreader.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class BookInfoDto(
    //val book_id: Int,
    val author: AuthorDto,
    val book: BookDto
//    val categories: List<Category>,
//    val tags: List<Tag>,
//    val author_name : String,
//    val author_id : Int,
)

