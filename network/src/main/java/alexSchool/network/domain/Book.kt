package alexSchool.network.domain

import alexSchool.network.dtos.ChapterDto

data class Book(
    val id: Int,
    val name: String,
    val pagesNumber: Int,
    val chaptersNumber: Int,
    val authorName: String,
    val description: String,
    val cover : ByteArray?,
    val index: String,
    val encoding: String,
    val bookSize: Float?,
    val targetLinks: String,
    val readingProgress: Int?,
    var chapters: MutableList<Chapter> = mutableListOf(),
) {
    fun addChapter(tokenBody: String, encoding: Encoding, chapterNumber: Int, bookIndex: List<ChapterDto>) {

//        val chapterPattern =
//            "${encoding.tags.tagStart}${encoding.tags.chapterTag}(.*?)${encoding.tags.tagEnd}"
//        val regex = Regex(chapterPattern, RegexOption.DOT_MATCHES_ALL)
//        regex.findAll(tokenBody).forEach { matchResult ->
//            val start = matchResult.range.first
//            val end = matchResult.range.last + 1
//            val chapterName = matchResult.groupValues[1].trim()
//            //Log.d("addChapter", "addChapter: $chapterName")
//            val chapterContent = tokenBody.substring(end)
//           // Log.d("addChapter", "addChapter: $chapterContent")
//
//        }
        val pageSplitPattern = "${encoding.tags.tagStart}${encoding.tags.pageTag}${encoding.tags.tagEnd}"
        println("pagesScreen : addChapter: pageSplitPattern is $pageSplitPattern")
        var pageNo = 1
        val pages = mutableListOf<Page>()
        tokenBody.split(pageSplitPattern).forEach {
            if (it.isNotEmpty())
                pages.add(Page(pageNo++, it))
        }
        val chapter = bookIndex.find { it.number == chapterNumber }
        chapters.add(Chapter(chapter!!.name, chapterNumber, pages))
        println("pagesScreen : addChapter: new chapter size is ${chapters.size}")
    }
}

