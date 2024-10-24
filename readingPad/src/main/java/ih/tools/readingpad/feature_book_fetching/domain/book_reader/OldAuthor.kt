package ih.tools.readingpad.feature_book_fetching.domain.book_reader

class OldAuthor : Id {
    var name: String
        private set

    /**
     * this fun will be used when create new author
     */
    constructor(name: String) {
        this.name = name
    }

    /**
     * this fun will be used when get author obj from database
     */
    constructor(id: Int, name: String) {
        this.id = id
        this.name = name
    }
}