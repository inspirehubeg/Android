package ih.tools.readingpad.feature_book_fetching.domain.book_reader

import alexSchool.network.domain.Encoding
import android.util.Log

data class OldBook(
    val oldBookInfo: OldBookInfo,
    val oldChapters: MutableList<OldChapter>
) {
    fun addChapter(tokenBody: String, oldEncoding: OldEncoding) {
        val chapterPattern =
            "${oldEncoding.oldTags.tagStart}${oldEncoding.oldTags.chapterTag}(.*?)\\*\\*"
        val regex = Regex(chapterPattern, RegexOption.DOT_MATCHES_ALL)
        regex.findAll(tokenBody).forEach { matchResult ->
            val start = matchResult.range.first
            val end = matchResult.range.last + 1
            val chapterName = matchResult.groupValues[1].trim()
            val chapterContent = tokenBody.substring(end)
            var pageNo = 1
            val oldPages = mutableListOf<OldPage>()
            chapterContent.split(oldEncoding.oldTags.pageTag).forEach {
                if (it.isNotEmpty())
                    oldPages.add(OldPage(pageNo++, Body(it)))
            }
            oldChapters.add(OldChapter(chapterName, oldChapters.size + 1, oldPages))
        }
    }
    fun addChapter(tokenBody: String, encoding: Encoding) {

        val chapterPattern =
            "${encoding.tags.tagStart}${encoding.tags.chapterTag}(.*?)${encoding.tags.tagEnd}"
        val regex = Regex(chapterPattern, RegexOption.DOT_MATCHES_ALL)
        regex.findAll(tokenBody).forEach { matchResult ->
            val start = matchResult.range.first
            val end = matchResult.range.last + 1
            val chapterName = matchResult.groupValues[1].trim()
            Log.d("addChapter", "addChapter: $chapterName")
            val chapterContent = tokenBody.substring(end)
            Log.d("addChapter", "addChapter: $chapterContent")
            var pageNo = 1
            val oldPages = mutableListOf<OldPage>()
            chapterContent.split(encoding.tags.pageTag).forEach {
                if (it.isNotEmpty())
                    oldPages.add(OldPage(pageNo++, Body(it)))
            }
            oldChapters.add(OldChapter(chapterName, oldChapters.size + 1, oldPages))
        }
    }
}