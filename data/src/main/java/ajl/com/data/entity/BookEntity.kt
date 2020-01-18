package ajl.com.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey var _id: Int,
    @ColumnInfo(name = "chapter_count") var chapterCount: Int,
    @ColumnInfo(name = "book_cat") var bookCategory: String,
    @ColumnInfo(name = "book_eng_name") var bookEngName: String,
    @ColumnInfo(name = "book_mal_name") var bookMalName: String,
    @ColumnInfo(name = "book_short_name") var bookShortName: String
)