package alexschool.bookreader.domain

data class DetailedBookInfo(
    val id: Int,
    val name: String,
    val cover: String,
    val description: String,
    val summary: String,
    val subscriptionId: Int,
    val pagesNumber: Int,
    val readingProgress: Int?,
    val bookRating: Double?,
    val releaseDate: String?,
    val publisherName: String?,
    val internationalNum: String?,
    val bookSize: Float,
    val categories: List<Category>,
    val authorsName: List<String>,
    val tags: List<Tag>,
    val translatorsName: List<String>
)
