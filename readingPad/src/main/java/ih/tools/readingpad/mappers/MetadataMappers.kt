package ih.tools.readingpad.mappers

import alexSchool.network.domain.Metadata
import alexSchool.network.dtos.MetadataDto
import alexSchool.network.entities.Converters
import alexSchool.network.entities.MetadataEntity
import android.util.Log

fun MetadataDto.toMetadataEntity(converters: Converters): MetadataEntity? {
    return try {
        Log.d("MetadataConversion", "Converting metadata: $encoding")
        MetadataEntity(
            bookId = book_id,
            encoding = converters.toEncoding(encoding),
            targetLinks = converters.toTargetLinkList(target_links),
            index = converters.toChapterDtoList(index)
        )
    } catch (e: Exception) {
        // Handle error, maybe log it or return null
        Log.e("MetadataConversion", "Error converting metadata: ${e.message}")
        null
    }
}

fun MetadataEntity.toMetadata(): Metadata {
    return Metadata(
        bookId = bookId,
        encoding = encoding,
        targetLinks = targetLinks,
        index = index
    )
}