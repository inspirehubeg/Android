package alexSchool.network.domain

data class Chapter(
    val title: String,
    val chapterNumber: Int,
    val pages: List<Page>
)
