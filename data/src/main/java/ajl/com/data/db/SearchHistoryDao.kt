package ajl.com.data.db

import ajl.com.data.entity.SearchHistoryEntity
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SearchHistoryDao {
    @Query("SELECT * FROM search_history ORDER BY date DESC")
    fun getAllSearchHistory(): List<SearchHistoryEntity>

    @Query("SELECT * FROM search_history WHERE result_count>0 ORDER BY date DESC")
    fun getValidSearchHistory(): List<SearchHistoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: SearchHistoryEntity): Long

    @Query("DELETE FROM search_history WHERE _id = :id")
    fun deleteSearch(id: Int)

    @Query("SELECT COUNT(_id) FROM search_history WHERE result_count>0")
    fun getNumberOfSearches(): Int
}