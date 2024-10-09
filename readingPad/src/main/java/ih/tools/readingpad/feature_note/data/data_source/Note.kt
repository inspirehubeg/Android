package ih.tools.readingpad.feature_note.data.data_source

data class Note(
    val id: Long?,
    val bookId: String,
    val chapterNumber: Int,
    val pageNumber: Int,
    val start: Int,
    val end: Int,
    val text: String
)
