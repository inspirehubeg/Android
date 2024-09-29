package alexschool.bookreader.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "book_info")
data class BookInfoEntity(
    @PrimaryKey(autoGenerate = false)
    val bookId: Int, //foreign key to book table
//    val categories: List<Category>,
//    val tags: List<Tag>,
//    val authorName : String,
    val authorId : Int,  //foreign key to author table
)