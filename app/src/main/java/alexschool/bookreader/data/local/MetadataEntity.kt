package alexschool.bookreader.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MetadataEntity(
    @PrimaryKey(autoGenerate = false)
    val bookId: Int,
    val encoding: String,
    val index: String,
   // val targetLinks: List<TargetLink>
)
