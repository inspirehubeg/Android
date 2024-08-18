package ih.tools.readingpad.feature_book_fetching.domain.book_reader

data class Book(val bookInfo: BookInfo, val chapters: MutableList<Chapter>) {
    fun addChapter(tokenBody: String, encoding: Encoding) {
        val chapterPattern = "${encoding.tags.tagStart}${encoding.tags.chapterTag}(.*?)\\*\\*"
        val regex = Regex(chapterPattern, RegexOption.DOT_MATCHES_ALL)
        regex.findAll(tokenBody).forEach { matchResult ->
            val start = matchResult.range.first
            val end = matchResult.range.last + 1
            val chapterName = matchResult.groupValues[1].trim()
            val chapterContent = tokenBody.substring(end)
            var pageNo = 1
            val pages = mutableListOf<Page>()
            chapterContent.split(encoding.tags.pageTag).forEach {
                if (it.isNotEmpty())
                pages.add(Page(pageNo++, Body(it)))
            }
            chapters.add(Chapter(chapterName, chapters.size + 1, pages))
        }
    }
}