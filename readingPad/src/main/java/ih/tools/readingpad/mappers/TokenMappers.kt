package ih.tools.readingpad.mappers

import alexSchool.network.data.Token
import alexSchool.network.dtos.TokenDto
import alexSchool.network.entities.TokenEntity

fun TokenDto.toTokenEntity(): TokenEntity {
    return TokenEntity(
        id = id,
        firstPageNumber = first_page_number,
        lastPageNumber = last_page_number,
        firstChapterNumber = first_chapter_number,
        lastChapterNumber = last_chapter_number,
        bookId = book_id,
        count = count,
        content = content,
        size = size
    )
}

fun TokenEntity.toToken(): Token {
    return Token(
        id = id,
        firstPageNumber = firstPageNumber,
        lastPageNumber = lastPageNumber,
        firstChapterNumber = firstChapterNumber,
        lastChapterNumber = lastChapterNumber,
        bookId = bookId,
        count = count,
        content = content,
        size = size
    )
}