package alexSchool.network.mappers

import alexSchool.network.domain.SavedBook
import alexSchool.network.dtos.SavedBookDto
import alexSchool.network.entities.SavedBookEntity

fun SavedBookDto.toSavedBookEntity(): SavedBookEntity {
    return SavedBookEntity(
        userId = user_id,
        bookId = book_id,
        type = type,
        savedAt = saved_at
    )
}

fun SavedBookEntity.toSavedBook(): SavedBook {
    return SavedBook(
        userId = userId,
        bookId = bookId,
        type = type
    )
}