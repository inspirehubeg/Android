package alexschool.bookreader.domain

data class GeneralBookInfo(
    val id: Int,
    val name: String,
    val cover: String,
    val authorsNames: List<String>,
    val rating: Double?,
    val readingProgress: Int?,
    val subscriptionId: Int,
)
