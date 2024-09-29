package alexschool.bookreader.data.mappers

import alexschool.bookreader.data.local.BookEntity
import alexschool.bookreader.data.remote.BookDto
import alexschool.bookreader.domain.Book

fun BookDto.toBookEntity(): BookEntity {
    return BookEntity(
        id = id,
        name = name,
        cover = cover,
        description = description,
        summary = summary,
       // authorId = author_id,
        pagesNumber = pages_number,
        chaptersNumber = chapters_number,
       // bookSize = book_size,
        subscriptionId = subscription_id,
        readingProgress = reading_progress,
        bookRating = book_rating,
        releaseDate = release_date
    )
}

fun BookEntity.toBook(): Book{
    return Book(
        id = id,
        name = name,
        cover = cover,
        description = description,
        summary = summary,
       // authorId = authorId,
        pagesNumber = pagesNumber,
        chaptersNumber = chaptersNumber,
        //bookSize = bookSize,
        subscriptionId = subscriptionId,
        readingProgress = readingProgress,
        bookRating = bookRating,
         releaseDate = releaseDate
    )
}