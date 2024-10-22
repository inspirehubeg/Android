package alexSchool.network.domain


data class ReadingProgress(
    val bookId: Int,
    val userId: Int,
    val progress: Int,
)
