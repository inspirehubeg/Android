package alexschool.bookreader.data.mappers

import alexschool.bookreader.data.local.BookProgressEntity
import alexschool.bookreader.data.remote.BookProgressDto
import alexschool.bookreader.domain.BookProgress

fun BookProgressDto.toBookProgressEntity(): BookProgressEntity{
    return BookProgressEntity(
        bookId = book_id,
        userId = user_id,
        progress = progress
    )
}

fun BookProgressEntity.toBookProgress(): BookProgress {
    return BookProgress(
        bookId = bookId,
        userId = userId,
        progress = progress
    )
}