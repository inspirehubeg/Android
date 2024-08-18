package ih.tools.readingpad.feature_book_fetching.domain.book_reader

import book_reader.Author
import com.google.gson.Gson

class BookInfo : Id {
    var name: String
        private set
    var cover: String
        private set
    var description: String
        private set
    var author: Author
        private set
    var pagesNumber: Int
        private set
    var chaptersNumber: Int
        private set
    var bookSize: Int
        private set


    /**
     * this fun will be used when create new bookInfo obj
     */
    constructor(
        id: String,
        name: String,
        cover: String,
        description: String,
        author: Author,
        pagesNumber: Int,
        bookSize: Int,
        chaptersNumber: Int
    ) {
        this.id = id
        this.name = name
        this.cover = cover
        this.description = description
        this.author = author
        this.pagesNumber = pagesNumber
        this.bookSize = bookSize
        this.chaptersNumber = chaptersNumber
    }

    /**
     * this fun will be used when get bookInfo obj from database
     */
    constructor(
        name: String,
        cover: String,
        description: String,
        author: Author,
        pagesNumber: Int,
        bookSize: Int,
        chaptersNumber: Int
    ) {
        this.name = name
        this.cover = cover
        this.description = description
        this.author = author
        this.pagesNumber = pagesNumber
        this.bookSize = bookSize
        this.chaptersNumber = chaptersNumber
    }

    companion object {
        fun instance(content: String): BookInfo {
            val gson = Gson()
            return gson.fromJson(content, BookInfo::class.java)
        }
    }


}