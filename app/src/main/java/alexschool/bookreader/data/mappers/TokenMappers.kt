package alexschool.bookreader.data.mappers

import alexschool.bookreader.domain.Token

fun alexschool.bookreader.data.remote.TokenDto.toTokenEntity(): alexschool.bookreader.data.local.TokenEntity {
    return alexschool.bookreader.data.local.TokenEntity(
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

fun alexschool.bookreader.data.local.TokenEntity.toToken(): Token {
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