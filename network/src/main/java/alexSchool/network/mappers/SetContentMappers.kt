package alexSchool.network.mappers

import alexSchool.network.domain.SetContent
import alexSchool.network.dtos.SetContentDto
import alexSchool.network.entities.SetContentEntity

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