package ajl.com.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "verses")
data class VerseEntity(
    @PrimaryKey var _id: Int,
    @ColumnInfo(name = "testament") var testament: String,
    @ColumnInfo(name = "book") var book: String,
    @ColumnInfo(name = "book_no") var book_no: Int,
    @ColumnInfo(name = "chapter") var chapter: Int,
    @ColumnInfo(name = "actual_chapter_no") var actual_chapter_no: Int,
    @ColumnInfo(name = "verse_no") var verse_no: Int,
    @ColumnInfo(name = "verse") var verse: String,
    @ColumnInfo(name = "heading") var heading: String
)