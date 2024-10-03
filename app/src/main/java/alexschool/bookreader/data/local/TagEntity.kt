package alexschool.bookreader.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity (tableName = "tags")
data class TagEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    //val version: Int,
)
