package alexSchool.network.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "publishers")
data class PublisherEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
)
