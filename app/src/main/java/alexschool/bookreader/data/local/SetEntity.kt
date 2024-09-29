package alexschool.bookreader.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SetEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String,
)
