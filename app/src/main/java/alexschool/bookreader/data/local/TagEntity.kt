package alexschool.bookreader.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class TagEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val name: String,
    val description: String,
)
