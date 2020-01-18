package ajl.com.data.db

import ajl.com.data.entity.FootnoteEntity
import ajl.com.data.entity.FootnoteVerseEntity
import androidx.room.Dao
import androidx.room.Query

@Dao
interface FoteNoteDao {

    @Query("SELECT * FROM footnotes LIMIT 10")
    fun getAll(): List<FootnoteEntity>

    @Query("SELECT _id,verse_id,footnote FROM footnotes WHERE verse_id==:verseID")
    fun getFootnoteFromVerse(verseID: Int): FootnoteVerseEntity

}