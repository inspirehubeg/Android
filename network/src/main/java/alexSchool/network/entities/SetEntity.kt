package alexSchool.network.entities

import androidx.room.Entity

@Entity(
    tableName = "sets",
    primaryKeys = ["name", "userId"]
)
data class SetEntity(
    val id: Int,
    val userId: Int,
    val name: String,
    //val version: Int
)
