package alexschool.bookreader.data.mappers

import alexschool.bookreader.data.local.TokenEntity
import alexschool.bookreader.data.remote.TokenDto
import alexschool.bookreader.domain.Token

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