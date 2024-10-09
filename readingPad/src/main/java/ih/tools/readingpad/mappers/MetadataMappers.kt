package ih.tools.readingpad.mappers

import alexSchool.network.dtos.MetadataDto
import alexSchool.network.entities.MetadataEntity

fun MetadataDto.toMetadataEntity(): MetadataEntity {
    return MetadataEntity(
        bookId = book_id,
        encoding = encoding,
        targetLinks = target_links,
        index = index
    )
}

fun MetadataEntity.toMetadata(): alexSchool.network.data.Metadata {
    return alexSchool.network.data.Metadata(
        bookId = bookId,
        encoding = encoding,
        targetLinks = targetLinks,
        index = index
    )
}