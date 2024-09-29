package alexschool.bookreader.data.mappers

import alexschool.bookreader.data.local.MetadataEntity
import alexschool.bookreader.data.remote.MetadataDto
import alexschool.bookreader.domain.Metadata

fun MetadataDto.toMetadataEntity(): MetadataEntity{
    return MetadataEntity(
        bookId = book_id,
        encoding = encoding,
        index = index,
//        targetLinks = target_links
    )
}

fun MetadataEntity.toMetadata(): Metadata {
    return alexschool.bookreader.domain.Metadata(
        bookId = bookId,
        encoding = encoding,
        index = index,
       // targetLinks = targetLinks
    )
}