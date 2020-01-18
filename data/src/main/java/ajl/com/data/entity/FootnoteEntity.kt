package ajl.com.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "footnotes")
data class FootnoteEntity(
    @PrimaryKey var _id: Int,
    @ColumnInfo(name = "testament") var testament: String,
    @ColumnInfo(name = "book_name") var book: String,
    @ColumnInfo(name = "book_id") var book_no: Int,
    @ColumnInfo(name = "chapter_no") var chapter_no: Int,
    @ColumnInfo(name = "verse_number") var verse_no: Int,
    @ColumnInfo(name = "verse_id") var verse_id: Int,
    @ColumnInfo(name = "footnote") var fote_note: String
)