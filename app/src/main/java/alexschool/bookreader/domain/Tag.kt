package alexschool.bookreader.domain

import kotlinx.serialization.Serializable

data class Tag(
    val id: String,
    val name: String,
    val description: String,

    )