package alexschool.bookreader.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BookProgressEntity (
    @PrimaryKey(autoGenerate = false)
    val bookId: Int,
    val userId: Int,
    val progress: Int,
)