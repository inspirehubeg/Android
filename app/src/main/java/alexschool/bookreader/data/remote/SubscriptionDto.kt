package alexschool.bookreader.data.remote

import kotlinx.serialization.Serializable


@Serializable
data class SubscriptionDto (
    val id: Int,
    val name: String,
    val image: String?,
    val is_deleted: Boolean?

)