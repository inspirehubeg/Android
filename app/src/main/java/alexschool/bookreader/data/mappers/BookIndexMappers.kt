package alexschool.bookreader.data.mappers

import alexschool.bookreader.data.local.BookIndexEntity
import alexschool.bookreader.data.remote.BookIndexDto
import alexschool.bookreader.domain.BookIndex

fun BookIndexDto.toBookIndexEntity(): BookIndexEntity {
    return BookIndexEntity(
        bookId = book_id,
        name = name,
        pageNumber = page_number,
        number = number
    )
}

fun BookIndexEntity.toBookIndex(): BookIndex {
    return BookIndex(
        bookId = bookId,
        name = name,
        pageNumber = pageNumber,
        number = number
    )
}