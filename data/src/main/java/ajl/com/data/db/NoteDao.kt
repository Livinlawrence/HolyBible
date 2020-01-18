package ajl.com.data.db

import ajl.com.data.entity.NoteEntity
import ajl.com.data.entity.VerseNoteEntity
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface NoteDao {

    @Query("SELECT v.*,b.note,b._id as note_id,v._id as verse_id,nf.foldername FROM notes b INNER JOIN  verses v ON  v._id=b.verse_id LEFT JOIN notes_folder nf ON b.folder_id = nf.id  WHERE nf.id = :noteFolderId ORDER BY v.book_no ASC")
    fun getAllNotes(noteFolderId: Int): List<VerseNoteEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: NoteEntity): Long

    @Query("DELETE FROM notes WHERE _id = :id")
    fun deleteNote(id: Int)

    @Query("UPDATE notes SET note= :note WHERE _id = :id")
    fun updateNote(id: Int, note: String)

    @Query("SELECT COUNT(verse_id) FROM notes")
    fun getNumberOfNotes(): Int

    @Query("DELETE FROM notes WHERE folder_id = :id")
    fun deleteFolderNote(id: Int)
}