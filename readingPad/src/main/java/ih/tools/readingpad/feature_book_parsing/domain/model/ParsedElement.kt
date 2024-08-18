package ih.tools.readingpad.feature_book_parsing.domain.model

/** different types of data in the book which would be parsed into a span*/
sealed class ParsedElement {
    /**regular text with no custom styling*/
    data class Text(val content: String) : ParsedElement()

    /** text with custom styling such as bold, italic, etc.*/
    data class Font(val content: String, val fontTag: String) : ParsedElement()

    /** weblinks */
    data class WebLink(val content: String) : ParsedElement()

    /** internal links that connects a source to target location */
    data class InternalLinkSource(var content: String, var key: String) : ParsedElement()

    /** images */
    data class Image(val content : ByteArray, val alignment: String, val ratio: Int) : ParsedElement()
}