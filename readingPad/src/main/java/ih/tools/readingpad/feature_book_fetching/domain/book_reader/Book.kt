package ih.tools.readingpad.feature_book_fetching.domain.book_reader

data class Book(val oldBookInfo: OldBookInfo, val chapters: MutableList<Chapter>) {
    fun addChapter(tokenBody: String, oldEncoding: OldEncoding) {
        val chapterPattern = "${oldEncoding.oldTags.tagStart}${oldEncoding.oldTags.chapterTag}(.*?)\\*\\*"
        val regex = Regex(chapterPattern, RegexOption.DOT_MATCHES_ALL)
        regex.findAll(tokenBody).forEach { matchResult ->
            val start = matchResult.range.first
            val end = matchResult.range.last + 1
            val chapterName = matchResult.groupValues[1].trim()
            val chapterContent = tokenBody.substring(end)
            var pageNo = 1
            val pages = mutableListOf<Page>()
            chapterContent.split(oldEncoding.oldTags.pageTag).forEach {
                if (it.isNotEmpty())
                pages.add(Page(pageNo++, Body(it)))
            }
            chapters.add(Chapter(chapterName, chapters.size + 1, pages))
        }
    }
}