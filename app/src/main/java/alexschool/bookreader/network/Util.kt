package alexschool.bookreader.network


object Util {
    const val BASE_URL="http://localhost:3000/api/v1/categories"
    const val BASE_URL_TEST="https://jsonplaceholder.typicode.com"
    const val POSTS_URL="$BASE_URL_TEST/posts"
    const val CATEGORIES_URL="http://192.168.240.241:8080/api/v1/categories?since=0"
    const val BOOK_INFO_URL="http://192.168.1.47:8080/api/v1/users/1/books?limit=2&offset=0"
    const val TOKEN_URL= "http://192.168.1.47:8080/api/v1/books/1/tokens/0"

    //const val BOOK_URL= "http://192.168.1.47:8080/api/v1/books/1"

    const val BOOK_INFO_ENDPOINT="books"
    const val CATEGORIES_ENDPOINT="categories"
    const val TAGS_ENDPOINT="tags"
    const val CATEGORIES_FOR_BOOK_ENDPOINT="books/:id/categories"
    const val TAGS_FOR_BOOK_ENDPOINT="books/:id/tags"
    const val BOOK_BY_ID_ENDPOINT="books/:id"
    const val METADATA_FOR_BOOK_ENDPOINT="books/:id/metadata"
    const val REVIEWS_FOR_BOOK_ENDPOINT="books/:id/reviews"
    const val RATING_FOR_BOOK_ENDPOINT="books/:id/rating"
    const val AUTHOR_FOR_BOOK_ENDPOINT="books/:id/author"
    const val TOKEN_FOR_BOOK_BY_TOKEN_ID_ENDPOINT="books/:id/tokens/:tokenId"
    const val BOOKS_BY_CATEGORY_ENDPOINT="categories/:categoryId/books"
    const val BOOKS_BY_TAG_ENDPOINT="tags/:tagId/books"
    const val BOOKS_BY_AUTHOR_ENDPOINT="authors/:authorId/books"

}

/**

 */