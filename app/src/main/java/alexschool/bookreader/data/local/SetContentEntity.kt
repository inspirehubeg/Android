package alexschool.bookreader.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class SetContentEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val bookId: Int,
)
