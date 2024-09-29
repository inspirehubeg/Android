package alexschool.bookreader.domain

import kotlinx.serialization.Serializable


data class BookProgress(
    val bookId: Int,
    val userId: Int,
    val progress: Int,
)
