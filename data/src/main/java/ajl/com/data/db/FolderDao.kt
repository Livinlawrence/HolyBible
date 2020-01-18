package ajl.com.data.db

import ajl.com.data.entity.BookmarkFolderEntity
import ajl.com.data.entity.NotesFolderEntity
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FolderDao {

    @Query("SELECT * FROM bookmark_folder")
    fun getAllBookmarkFolder(): List<BookmarkFolderEntity>

    @Query("SELECT * FROM notes_folder")
    fun getAllNotesFolder(): List<NotesFolderEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBookmarkFolder(entity: BookmarkFolderEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNotesFolder(entity: NotesFolderEntity): Long

    @Query("DELETE FROM bookmark_folder WHERE id = :id")
    fun deleteBookmarkFolder(id: Int)

    @Query("DELETE FROM notes_folder WHERE id = :id")
    fun deleteNoteFolder(id: Int)
}