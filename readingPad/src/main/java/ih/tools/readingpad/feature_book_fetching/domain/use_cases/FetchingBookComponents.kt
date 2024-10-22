package ih.tools.readingpad.feature_book_fetching.domain.use_cases

import android.content.Context
import ih.tools.readingpad.R
import ih.tools.readingpad.feature_book_fetching.domain.book_reader.OldBook
import ih.tools.readingpad.feature_book_fetching.domain.book_reader.OldBookInfo
import ih.tools.readingpad.feature_book_fetching.domain.book_reader.OldChapter
import ih.tools.readingpad.feature_book_fetching.domain.book_reader.OldMetadata

//Gets a file from raw directory
fun readFileFromRaw(context: Context, rawResourceId: Int): String {
    val inputStream = context.resources.openRawResource(rawResourceId)
    return inputStream.bufferedReader().use { it.readText() }
}

//called when the user selects a book to open
fun fetchBook(oldBookInfo: OldBookInfo ): OldBook {
    val newBook = getNewBook(oldBookInfo)
    // returns an empty book! that needs to get chapters
    return newBook
}


fun getBookInfo(context: Context): OldBookInfo {
    //this should be changed with a function that fetches from db
    val bookInfoContent = readFileFromRaw(context, R.raw.book_info)
    val oldBookInfo = OldBookInfo.instance(bookInfoContent)
    return oldBookInfo
}

fun getMetadata(context: Context): OldMetadata {
    val metadataContent = readFileFromRaw(context, R.raw.metadata)
    val oldMetadata = OldMetadata.instance(metadataContent)
    return oldMetadata
}

fun getRawFileByName(context: Context, fileName: String): Int? {
    return try {
         context.resources.getIdentifier(fileName, "raw", context.packageName)
    } catch (e: Exception) {
        // Handle the case where the file is not found
        null
    }
}
fun getNewBook(oldBookInfo: OldBookInfo): OldBook {
    val bookOldChapters = mutableListOf<OldChapter>()
    val oldBook = OldBook(oldBookInfo, bookOldChapters)
    return oldBook
}



