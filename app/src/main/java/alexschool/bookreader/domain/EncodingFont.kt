package alexschool.bookreader.domain

import kotlinx.serialization.Serializable


data class EncodingFont(
    val bold: String? = null,
    val italic: String? = null,
    val size: String? = null,
    val fontColor: String? = null,
    val backgroundColor: String? = null,
    val align: String? = null,
    val underline: String? = null,
)