package ajl.com.data.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "tags", indices = arrayOf(Index(value = ["verseid"], unique = true)))
data class ColorTagEntity(
    @ColumnInfo(name = "color_tag") var colorTag: Int,
    @ColumnInfo(name = "verseid") var verseNo: Int
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var _id: Int = 0

    constructor(source: Parcel) : this(
        source.readInt(),
        source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(colorTag)
        writeInt(verseNo)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ColorTagEntity> = object : Parcelable.Creator<ColorTagEntity> {
            override fun createFromParcel(source: Parcel): ColorTagEntity = ColorTagEntity(source)
            override fun newArray(size: Int): Array<ColorTagEntity?> = arrayOfNulls(size)
        }
    }
}