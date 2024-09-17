package ih.tools.readingpad.feature_book_fetching.domain.book_reader

import book_reader.Offset
import com.google.gson.Gson

data class Metadata(val encoding: Encoding, val tokenOffsets: List<Offset>, val targetLinks: List<TargetLink>) {
    companion object {
        fun instance(content: String): Metadata {
            val gson = Gson()
            return gson.fromJson(content, Metadata::class.java)
        }
    }
}