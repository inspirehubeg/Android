package ih.tools.readingpad.mappers

import alexSchool.network.dtos.HighlightDto
import ih.tools.readingpad.feature_highlight.data.data_source.Highlight
import ih.tools.readingpad.feature_highlight.data.data_source.HighlightEntity

fun HighlightDto.toHighlightEntity(): HighlightEntity {
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

fun HighlightEntity.toHighlight(): Highlight {
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