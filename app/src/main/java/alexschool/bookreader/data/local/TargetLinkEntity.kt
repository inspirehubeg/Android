package alexschool.bookreader.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TargetLinkEntity(
    @PrimaryKey
    val key: String,
    val chapterNumber: Int,
    val bookId: Int,
    val pageNumber: Int,
    val index: Int
)
