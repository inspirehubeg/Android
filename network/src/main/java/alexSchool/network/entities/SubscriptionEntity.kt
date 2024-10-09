package alexSchool.network.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subscriptions")
data class SubscriptionEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val image: String?,
    val isDeleted: Boolean?,
)
