package ajl.com.data.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmark_folder")
data class BookmarkFolderEntity(
    @ColumnInfo(name = "foldername") var foldername: String
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    constructor(source: Parcel) : this(
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(foldername)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<BookmarkFolderEntity> = object : Parcelable.Creator<BookmarkFolderEntity> {
            override fun createFromParcel(source: Parcel): BookmarkFolderEntity = BookmarkFolderEntity(source)
            override fun newArray(size: Int): Array<BookmarkFolderEntity?> = arrayOfNulls(size)
        }
    }
}