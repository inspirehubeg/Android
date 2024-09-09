package ih.tools.readingpad.feature_note.domain.use_cases

data class NoteUseCases (
    val addNote: AddNote,
    val deleteNoteById: DeleteNoteById,
    val getBookNotes: GetBookNotes,
    val getPageNotes: GetPageNotes,
    val updateNoteText: UpdateNoteText
)
