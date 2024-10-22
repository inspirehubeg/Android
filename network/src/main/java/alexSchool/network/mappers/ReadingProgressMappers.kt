package alexSchool.network.mappers

import alexSchool.network.domain.ReadingProgress
import alexSchool.network.dtos.ReadingProgressDto
import alexSchool.network.entities.ReadingProgressEntity

fun ReadingProgressDto.toReadingProgressEntity(): ReadingProgressEntity {
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