package alexSchool.network.domain

data class DetailedBookInfo(
    val id: Int,
    val name: String,
    val cover: ByteArray,
    val description: String,
    val summary: String,
    val subscriptionId: Int,
    val pagesNumber: Int,
    val chaptersNumber: Int,
    val readingProgress: Int?,
    val bookRating: Double?,
    val releaseDate: String?,
    val publisherName: List<String>,
    val internationalNum: String?,
    val language: String,
    val bookSize: Float?,
    val categories: List<Category>,
    val authorsName: List<String>,
    val tags: List<Tag>,
    val translatorsName: List<String>
)
