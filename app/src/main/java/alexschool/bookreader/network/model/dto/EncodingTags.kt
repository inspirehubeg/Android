package alexschool.bookreader.network.model.dto

import kotlinx.serialization.Serializable


@Serializable
data class EncodingTags(
    val tagStart: String,
    val tagEnd: String,
    val tagLength: Int,
    val formatLength: Int,
    val linkKeyLength: Int,
    val webLink: String,
    val internalLink: String,
    val internalLinkTarget: String,
    val image: String,
    val pageTag: String,
    val chapterTag: String,
    val splitTag: String
)
