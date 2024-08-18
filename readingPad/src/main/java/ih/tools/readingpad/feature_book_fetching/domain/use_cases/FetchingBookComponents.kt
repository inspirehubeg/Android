package ih.tools.readingpad.feature_book_fetching.domain.use_cases

import android.content.Context
import ih.tools.readingpad.R
import ih.tools.readingpad.feature_book_fetching.domain.book_reader.Book
import ih.tools.readingpad.feature_book_fetching.domain.book_reader.BookInfo
import ih.tools.readingpad.feature_book_fetching.domain.book_reader.Chapter
import ih.tools.readingpad.feature_book_fetching.domain.book_reader.Metadata

//Gets a file from raw directory
fun readFileFromRaw(context: Context, rawResourceId: Int): String {
    val inputStream = context.resources.openRawResource(rawResourceId)
    return inputStream.bufferedReader().use { it.readText() }
}

//called when the user selects a book to open
fun fetchBook(bookInfo: BookInfo ): Book {
    val newBook = getNewBook(bookInfo)
    // returns an empty book! that needs to get chapters
    return newBook
}


fun getBookInfo(context: Context): BookInfo {
    //this should be changed with a function that fetches from db
    val bookInfoContent = readFileFromRaw(context, R.raw.book_info)
    val bookInfo = BookInfo.instance(bookInfoContent)
    return bookInfo
}

fun getMetadata(context: Context): Metadata {
    val metadataContent = readFileFromRaw(context, R.raw.metadata)
    val metadata = Metadata.instance(metadataContent)
    return metadata
}

fun getRawFileByName(context: Context, fileName: String): Int? {
    return try {
         context.resources.getIdentifier(fileName, "raw", context.packageName)
    } catch (e: Exception) {
        // Handle the case where the file is not found
        null
    }
}
fun getNewBook(bookInfo: BookInfo): Book {
    val bookChapters = mutableListOf<Chapter>()
    val book = Book(bookInfo, bookChapters)
    return book
}



