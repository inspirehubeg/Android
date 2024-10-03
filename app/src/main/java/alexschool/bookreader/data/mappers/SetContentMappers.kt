package alexschool.bookreader.data.mappers

import alexschool.bookreader.data.local.SetContentEntity
import alexschool.bookreader.data.remote.SetContentDto
import alexschool.bookreader.domain.SetContent

fun SetContentDto.toSetContentEntity(): SetContentEntity {
    return SetContentEntity(
        setId = id,
        bookId = book_id,
    )
}

fun SetContentEntity.toSetContent(): SetContent {
    return SetContent(
        id = setId,
        bookId = bookId,
    )
}