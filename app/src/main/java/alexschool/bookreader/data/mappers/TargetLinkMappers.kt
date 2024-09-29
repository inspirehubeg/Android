package alexschool.bookreader.data.mappers

import alexschool.bookreader.data.local.TargetLinkEntity
import alexschool.bookreader.data.remote.TargetLinkDto
import alexschool.bookreader.domain.TargetLink

fun TargetLinkDto.toTargetLinkEntity(): TargetLinkEntity {
    return TargetLinkEntity(
        key = key,
        chapterNumber = chapter_number,
        pageNumber = page_number,
        bookId = book_id,
        index = index
    )
}

fun TargetLinkEntity.toTargetLink(): TargetLink {
    return TargetLink(
        key = key,
        chapterNumber = chapterNumber,
        pageNumber = pageNumber,
        bookId = bookId,
        index = index
    )
}