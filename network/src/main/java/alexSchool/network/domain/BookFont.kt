package alexSchool.network.domain

import kotlinx.serialization.Serializable

@Serializable
data class BookFont(
    val name: String? = null,
    val bold: String? = null,
    val italic: String? = null,
    val size: String? = null,
    val fontColor: String,
    val backgroundColor: String,
    val align: String? = null,
    val underline: String? = null,
    val fontFamilyName: String? = null
)
