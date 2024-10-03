package alexschool.bookreader.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "authors")
data class AuthorEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val bio: String,
    val image: String?,
  //  val version: Int
)
