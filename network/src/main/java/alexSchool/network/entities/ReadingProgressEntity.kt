package alexSchool.network.entities

import androidx.room.Entity

@Entity(
    tableName = "reading_progress",
    primaryKeys = ["bookId", "userId"]
)
data class ReadingProgressEntity(
    val bookId: Int,
    val userId: Int,
    val progress: Int,
    //val version: Int
)