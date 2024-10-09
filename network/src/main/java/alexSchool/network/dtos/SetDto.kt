package alexSchool.network.dtos

import kotlinx.serialization.Serializable

@Serializable
data class SetDto(
    val id: Int,
    val name: String,
    val user_id: Int,
    val is_deleted: Boolean?,
    val last_updated: Int

)
