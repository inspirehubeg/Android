package ih.tools.readingpad.feature_book_parsing.domain.model

import android.text.SpannableStringBuilder

data class SpannedPage(
    val content: SpannableStringBuilder,
    val pageNumber: Int
)


