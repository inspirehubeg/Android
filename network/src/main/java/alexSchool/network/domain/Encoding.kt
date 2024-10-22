package alexSchool.network.domain

import kotlinx.serialization.Serializable

@Serializable

data class Encoding(
    val tags: BookTags,
    val fonts: Map<String, BookFont>
)
