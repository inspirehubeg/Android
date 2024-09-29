package alexschool.bookreader.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "authors")
data class AuthorEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String,
   // val description: String,
)
