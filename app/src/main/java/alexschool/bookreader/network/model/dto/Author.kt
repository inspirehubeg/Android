package alexschool.bookreader.network.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class Author(
    val name: String,
    val description: String,
)