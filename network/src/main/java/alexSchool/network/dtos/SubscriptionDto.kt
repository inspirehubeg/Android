package alexSchool.network.dtos

import kotlinx.serialization.Serializable


@Serializable
data class SubscriptionDto (
    val id: Int,
    val name: String,
    val image: String?,
    val is_deleted: Boolean?,
    val last_updated: Int

)