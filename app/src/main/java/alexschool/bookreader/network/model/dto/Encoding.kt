package alexschool.bookreader.network.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class Encoding(val tags: EncodingTags, val fonts: Map<String, EncodingFont>)
