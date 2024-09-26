package alexschool.bookreader.network.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class Tag(
    val id: String,
    val name: String,
    val description: String,

    )