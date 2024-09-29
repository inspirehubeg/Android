package alexschool.bookreader.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BookIndexEntity(
    @PrimaryKey(autoGenerate = false)
    val bookId: Int,
    val name: String,
    val pageNumber: Int,
    val number: Int,
)
