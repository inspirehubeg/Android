package book_reader

import ih.tools.readingpad.feature_book_fetching.domain.book_reader.Id

class Author : Id {
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
    constructor(id: String, name: String) {
        this.id = id
        this.name = name
    }
}