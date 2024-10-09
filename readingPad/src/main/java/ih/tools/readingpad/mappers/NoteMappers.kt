package ih.tools.readingpad.mappers

import alexSchool.network.dtos.NoteDto
import ih.tools.readingpad.feature_note.data.data_source.Note
import ih.tools.readingpad.feature_note.data.data_source.NoteEntity

fun NoteDto.toNoteEntity(): NoteEntity {
    return NoteEntity(
        id = id,
        bookId = book_id,
        chapterNumber = chapter_number,
        pageNumber = page_number,
        start = start,
        end = end,
        text = text
    )

}

fun NoteEntity.toNote(): Note {
    return Note(
        id = id,
        bookId = bookId,
        chapterNumber = chapterNumber,
        pageNumber = pageNumber,
        start = start,
        end = end,
        text = text
    )

}