package alexschool.bookreader.domain

import kotlinx.serialization.Serializable

data class Encoding(
    val tags: EncodingTags,
    val fonts: Map<String, EncodingFont>
)
