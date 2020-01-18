package ajl.com.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks", indices = arrayOf(Index(value = ["verse_id"], unique = true)))
data class BookmarkEntity(
    var verse_id: Int, var addedon: Long, var folder_id: Int
) {
    @PrimaryKey(autoGenerate = true)
    var _id: Int = 0
}