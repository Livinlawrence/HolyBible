package ajl.com.data.db

import ajl.com.data.entity.BookEntity
import androidx.room.Dao
import androidx.room.Query

@Dao
interface BookDao {

    @Query("SELECT * FROM books")
    fun getAll(): List<BookEntity>

    @Query("SELECT * FROM books WHERE book_cat='OLD'")
    fun getAllOldBooks(): List<BookEntity>

    @Query("SELECT * FROM books WHERE book_cat='NEW'")
    fun getAllNewBooks(): List<BookEntity>

}