package alexschool.bookreader.domain



data class TargetLink(
    val key: String,
    val chapterNumber: Int,
    val bookId: Int,
    val pageNumber: Int,
    val index: Int
)
