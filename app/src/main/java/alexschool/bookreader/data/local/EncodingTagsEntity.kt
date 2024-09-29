package alexschool.bookreader.data.local

data class EncodingTagsEntity(
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
