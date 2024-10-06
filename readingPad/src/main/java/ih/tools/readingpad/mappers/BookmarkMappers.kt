package ih.tools.readingpad.mappers

import ih.tools.readingpad.feature_bookmark.domain.model.Bookmark
import ih.tools.readingpad.feature_bookmark.domain.model.BookmarkEntity
import ih.tools.readingpad.remote.BookmarkDto

fun BookmarkDto.toBookmarkEntity(): BookmarkEntity {
    return BookmarkEntity(
        id = id,
        bookId = bookId,
        pageNumber = page_number,
        chapterNumber = chapter_number,
        start = start,
        end = end,
        bookmarkTitle = bookmark_title
    )
}

fun BookmarkEntity.toBookmark(): Bookmark {
    return Bookmark(
        id = id,
        bookId = bookId,
        pageNumber = pageNumber,
        chapterNumber = chapterNumber,
        start = start,
        end = end,
        bookmarkTitle = bookmarkTitle
    )
}