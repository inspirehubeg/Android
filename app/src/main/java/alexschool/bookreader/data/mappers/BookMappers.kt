package alexschool.bookreader.data.mappers

import alexschool.bookreader.data.local.BookEntity
import alexschool.bookreader.data.local.BookWithDetails
import alexschool.bookreader.data.remote.BookDto
import alexschool.bookreader.domain.Book
import alexschool.bookreader.domain.DetailedBookInfo
import alexschool.bookreader.domain.GeneralBookInfo

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
        bookSize = book_size,
        subscriptionId = subscription_id,
        readingProgress = reading_progress,
        bookRating = book_rating,
        releaseDate = release_date,
        encoding = encoding,
        index = index,
        targetLinks = target_links,
        publisherName = publisher_name,
        internationalNum = international_num,
        language = language,
        lastOpened = null //this value should be fetched from local db
    )
}

fun BookEntity.toGeneralBookInfo(bookWithDetails: BookWithDetails): GeneralBookInfo {
    return GeneralBookInfo(
        id = id,
        name = name,
        cover = cover,
        authorsNames = bookWithDetails.authors.map { it.name },
        rating = bookRating,
        readingProgress = readingProgress,
        subscriptionId = subscriptionId
    )
}

fun BookEntity.toDetailedBookInfo(bookWithDetails: BookWithDetails): DetailedBookInfo {
    return DetailedBookInfo(
        id = id,
        name = name,
        cover = cover,
        description = description,
        summary = summary,
        subscriptionId = subscriptionId,
        pagesNumber = pagesNumber,
        readingProgress = readingProgress,
        bookRating = bookRating,
        releaseDate = releaseDate,
        internationalNum = internationalNum,
        bookSize = bookSize,
        categories = bookWithDetails.categories.map { it.toCategory() },
        authorsName = bookWithDetails.authors.map { it.name },
        tags = bookWithDetails.tags.map { it.toTag() },
        translatorsName = bookWithDetails.translators.map { it.name },
        publisherName = publisherName
    )
}

fun BookEntity.toBook(): Book {
    return Book(
        id = id,
        name = name,
        encoding = encoding,
        index = index,
        targetLinks = targetLinks,
        pagesNumber = pagesNumber,
        chaptersNumber = chaptersNumber,
        bookSize = bookSize,
        tokens = null,
        readingProgress = readingProgress,

        )
}