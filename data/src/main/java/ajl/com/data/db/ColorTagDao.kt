package ajl.com.data.db

import ajl.com.data.entity.ColorTagEntity
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy


@Dao
interface ColorTagDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: ColorTagEntity): Long


}