package alexSchool.network.mappers

import alexSchool.network.domain.Book
import alexSchool.network.domain.BookInfo
import alexSchool.network.domain.DetailedBookInfo
import alexSchool.network.domain.GeneralBookInfo
import alexSchool.network.dtos.BookDto
import alexSchool.network.entities.BookInfoEntity
import alexSchool.network.entities.cross_tables.BookWithDetails

fun BookInfoEntity.toBookInfo(): BookInfo {
    return BookInfo(
        id = id,
        name = name,
        description = description,
        summary = summary,
        pagesNumber = pagesNumber,
        chaptersNumber = chaptersNumber,
        readingProgress = readingProgress,
        subscriptionId = subscriptionId,
        releaseDate = releaseDate,
        bookRating = bookRating,
        cover = cover,
        internationalNum = internationalNum,
        language = language,
        size = size,
        isDeleted = isDeleted,
        lastOpened = lastOpened
    )
}

fun BookDto.toBookInfoEntity(): BookInfoEntity {
    val book = book
    return BookInfoEntity(
        id = book.id,
        name = book.name,
        description = book.description,
        summary = book.summary,
        pagesNumber = book.pages_number,
        chaptersNumber = book.chapters_number,
        readingProgress = book.reading_progress,
        subscriptionId = book.subscription_id,
        releaseDate = book.release_date,
        bookRating = book.book_rating,
        cover = book.cover,
        internationalNum = book.international_num,
        language = book.language,
        size = book.size,
        isDeleted = book.is_deleted,
        lastOpened = null
    )
}
//fun BookDto.toBookEntity(): BookEntity {
//    return BookEntity(
//        id = id,
//        name = name,
//        cover = cover,
//        description = description,
//        summary = summary,
//        // authorId = author_id,
//        pagesNumber = pages_number,
//        chaptersNumber = chapters_number,
//        bookSize = book_size,
//        subscriptionId = subscription_id,
//        readingProgress = reading_progress,
//        bookRating = book_rating,
//        releaseDate = release_date,
//        encoding = encoding,
//        index = index,
//        targetLinks = target_links,
//        publisherName = publisher_name,
//        internationalNum = international_num,
//        language = language,
//        lastOpened = null //this value should be fetched from local db
//    )
//}

fun BookInfo.toGeneralBookInfo(bookWithDetails: BookWithDetails): GeneralBookInfo {
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

fun BookInfoEntity.toDetailedBookInfo(bookWithDetails: BookWithDetails): DetailedBookInfo {
    return DetailedBookInfo(
        id = id,
        name = name,
        cover = java.util.Base64.getDecoder().decode(cover),
        description = description,
        summary = summary,
        subscriptionId = subscriptionId,
        pagesNumber = pagesNumber,
        chaptersNumber= chaptersNumber,
        readingProgress = readingProgress,
        bookRating = bookRating,
        releaseDate = releaseDate,
        internationalNum = internationalNum,
        bookSize = size,
        categories = bookWithDetails.categories.map { it.toCategory() },
        authorsName = bookWithDetails.authors.map { it.name },
        tags = bookWithDetails.tags.map { it.toTag() },
        translatorsName = bookWithDetails.translators.map { it.name },
        publisherName = bookWithDetails.publishers.map { it.name },
        language = language
    )
}

fun BookInfo.toBook(): Book {
    return Book(
        id = id,
        name = name,
        description =description,
        authorName = "",
        encoding = "",
        index = "",
        targetLinks = "",
        pagesNumber = pagesNumber,
        chaptersNumber = chaptersNumber,
        bookSize = size,
        chapters = mutableListOf(),
        readingProgress = readingProgress,
        cover = java.util.Base64.getDecoder().decode(cover)
    )
}