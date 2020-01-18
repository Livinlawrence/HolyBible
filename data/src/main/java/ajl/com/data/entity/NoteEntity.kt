package ajl.com.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "notes", indices = arrayOf(Index(value = ["verse_id"], unique = true)))
data class NoteEntity(
    var verse_id: Int,
    var note: String,
    var folder_id: Int
) {
    @PrimaryKey(autoGenerate = true)
    var _id: Int = 0
}