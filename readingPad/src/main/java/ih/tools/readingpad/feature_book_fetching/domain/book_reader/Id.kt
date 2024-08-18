package ih.tools.readingpad.feature_book_fetching.domain.book_reader

import java.util.*

abstract class Id {
    var id: String = UUID.randomUUID().toString()
        protected set
}