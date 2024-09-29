package alexschool.bookreader.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SavedBookEntity(
    @PrimaryKey(autoGenerate = false)
    val userId: Int,
    val bookId: Int,
    val type: String,
)
