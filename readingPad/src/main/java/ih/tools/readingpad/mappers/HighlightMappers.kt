package ih.tools.readingpad.mappers

import ih.tools.readingpad.feature_highlight.domain.model.Highlight
import ih.tools.readingpad.feature_highlight.domain.model.HighlightEntity
import ih.tools.readingpad.remote.HighlightDto

fun HighlightDto.toHighlightEntity(): HighlightEntity{
    return HighlightEntity(
        id = id,
        bookId = book_id,
        chapterNumber = chapter_number,
        pageNumber = page_number,
        start = start,
        end = end,
        text = text,
        color = color
    )
}

fun HighlightEntity.toHighlight(): Highlight{
    return Highlight(
        id = id,
        bookId = bookId,
        chapterNumber = chapterNumber,
        pageNumber = pageNumber,
        start = start,
        end = end,
        text = text,
        color = color
    )
}