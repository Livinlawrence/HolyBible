package ajl.com.data.db

import ajl.com.data.entity.BookChapterDetailEntity
import androidx.room.Dao
import androidx.room.Query

@Dao
interface BookChapterDetailsDao {

    @Query("SELECT * FROM book_chapter_dtls WHERE book_id == :bookId")
    fun getChapters(bookId: Int): List<BookChapterDetailEntity>

}