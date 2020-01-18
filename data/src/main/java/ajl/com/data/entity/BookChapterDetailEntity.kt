package ajl.com.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "book_chapter_dtls")
data class BookChapterDetailEntity(
    @PrimaryKey var _id: Int,
    @ColumnInfo(name = "book_id") var book_id: Int,
    @ColumnInfo(name = "chapter_no") var chapter_no: Int,
    @ColumnInfo(name = "verse_count") var verse_count: Int
)