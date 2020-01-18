package ajl.com.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "search_history",
    indices = [Index(value = ["_id"], unique = true), Index(
        value = ["search_keyword"],
        unique = true
    )]
)
data class SearchHistoryEntity(
    @ColumnInfo(name = "search_keyword") var keyword: String,
    @ColumnInfo(name = "date") var dateInMillis: Long,
    @ColumnInfo(name = "result_count") var resultNo: Int
) {
    @PrimaryKey(autoGenerate = true)
    var _id: Int = 0
}