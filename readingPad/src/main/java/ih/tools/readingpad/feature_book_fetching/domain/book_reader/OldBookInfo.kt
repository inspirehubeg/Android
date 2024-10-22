package ih.tools.readingpad.feature_book_fetching.domain.book_reader

import com.google.gson.Gson

class OldBookInfo : Id {
    var name: String
        private set
    var cover: String
        private set
    var description: String
        private set
    var oldAuthor: OldAuthor
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
        id: Int,
        name: String,
        cover: String,
        description: String,
        oldAuthor: OldAuthor,
        pagesNumber: Int,
        bookSize: Int,
        chaptersNumber: Int
    ) {
        this.id = id
        this.name = name
        this.cover = cover
        this.description = description
        this.oldAuthor = oldAuthor
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
        oldAuthor: OldAuthor,
        pagesNumber: Int,
        bookSize: Int,
        chaptersNumber: Int
    ) {
        this.name = name
        this.cover = cover
        this.description = description
        this.oldAuthor = oldAuthor
        this.pagesNumber = pagesNumber
        this.bookSize = bookSize
        this.chaptersNumber = chaptersNumber
    }

    companion object {
        fun instance(content: String): OldBookInfo {
            val gson = Gson()
            return gson.fromJson(content, OldBookInfo::class.java)
        }
    }


}