package alexschool.bookreader.data.mappers

import alexschool.bookreader.data.local.TokenEntity
import alexschool.bookreader.data.remote.TokenDto
import alexschool.bookreader.domain.Token

fun TokenDto.toTokenEntity(): TokenEntity {
    return TokenEntity(
        id = id,
        firstPage = first_page,
        lastPage = last_page,
        firstChapter = first_chapter,
        lastChapter = last_chapter,
        bookId = book_id,
        count = count,
        content = content
    )
}

fun TokenEntity.toToken(): Token {
    return Token(
        id = id,
        firstPage = firstPage,
        lastPage = lastPage,
        firstChapter = firstChapter,
        lastChapter = lastChapter,
        bookId = bookId,
        count = count,
        content = content
    )
}