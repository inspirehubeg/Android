package alexschool.bookreader.data.mappers

import alexschool.bookreader.data.local.ReadingProgressEntity
import alexschool.bookreader.data.remote.ReadingProgressDto
import alexschool.bookreader.domain.ReadingProgress

fun ReadingProgressDto.toReadingProgressEntity(): ReadingProgressEntity{
    return ReadingProgressEntity(
        bookId = book_id,
        userId = user_id,
        progress = progress
    )
}

fun ReadingProgressEntity.toReadingProgress(): ReadingProgress {
    return ReadingProgress(
        bookId = bookId,
        userId = userId,
        progress = progress
    )
}