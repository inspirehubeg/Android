package alexSchool.network.entities

import androidx.room.Entity


@Entity(
    tableName = "setContent",
    primaryKeys = ["setId", "bookId"]
)
data class SetContentEntity(
    val setId: Int,
    val bookId: Int,
    //val version: Int
)
