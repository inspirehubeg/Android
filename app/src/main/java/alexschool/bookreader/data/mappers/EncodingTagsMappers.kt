package alexschool.bookreader.data.mappers

import alexschool.bookreader.data.local.EncodingTagsEntity
import alexschool.bookreader.data.remote.EncodingTagsDto
import alexschool.bookreader.domain.EncodingTags

fun EncodingTagsDto.EncodingTagsEntity(): EncodingTagsEntity {
    return EncodingTagsEntity(
        tagStart = tagStart,
        tagEnd = tagEnd,
        splitTag = splitTag,
        chapterTag = chapterTag,
        pageTag = pageTag,
        image = image,
        internalLinkTarget = internalLinkTarget,
        internalLink = internalLink,
        webLink = webLink,
        linkKeyLength = linkKeyLength,
        formatLength = formatLength,
        tagLength = tagLength
    )
}

fun EncodingTagsEntity.toEncodingTags(): EncodingTags {
    return EncodingTags(
        tagStart = tagStart,
        tagEnd = tagEnd,
        splitTag = splitTag,
        chapterTag = chapterTag,
        pageTag = pageTag,
        image = image,
        internalLinkTarget = internalLinkTarget,
        internalLink = internalLink,
        webLink = webLink,
        linkKeyLength = linkKeyLength,
        formatLength = formatLength,
        tagLength = tagLength
    )

}