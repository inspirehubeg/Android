package alexschool.bookreader.data.mappers

import alexschool.bookreader.data.local.BookInfoEntity
import alexschool.bookreader.data.remote.BookInfoDto
import alexschool.bookreader.domain.BookInfo

fun BookInfoDto.toBookInfoEntity(): BookInfoEntity {
    return BookInfoEntity(
        bookId = book.id,
//        categories = categories,
//        tags = tags,
    //    authorName = author_name,
        authorId = author.id
    )
}

fun BookInfoEntity.toBookInfo(): BookInfo {
    return BookInfo(
        bookId = bookId,
//        categories = categories,
//        tags = tags,
      //  authorName = ,
        authorId = authorId
    )
}