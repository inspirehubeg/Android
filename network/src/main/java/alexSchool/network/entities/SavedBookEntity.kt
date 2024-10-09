package alexSchool.network.entities

import androidx.room.Entity
import androidx.room.TypeConverters
import java.time.Instant

@Entity(
    tableName = "saved_books",
    primaryKeys = ["userId", "bookId"]
)
@TypeConverters(InstantConverter::class)
data class SavedBookEntity(
    val userId: Int,
    val bookId: Int,
    val type: String, //wishlist, saved for later, etc.
   // val version: Int,
    val savedAt: Instant?
)
