package alexschool.bookreader.data.mappers

import alexschool.bookreader.data.local.SavedBookEntity
import alexschool.bookreader.data.remote.SavedBookDto
import alexschool.bookreader.domain.SavedBook

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