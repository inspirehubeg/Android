package ih.tools.readingpad.feature_book_fetching.domain.book_reader

import com.google.gson.Gson

data class OldMetadata(val oldEncoding: OldEncoding, val tokenOffsets: List<Offset>, val oldTargetLinks: List<OldTargetLink>) {
    companion object {
        fun instance(content: String): OldMetadata {
            val gson = Gson()
            return gson.fromJson(content, OldMetadata::class.java)
        }
    }
}