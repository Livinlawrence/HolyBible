package ajl.com.domain.entities

import android.os.Parcel
import android.os.Parcelable

data class ChapterDisplayEntity(
    var _id: Int,
    var bookId: Int,
    var bookCategory: String,
    var bookEngName: String,
    var bookMalName: String,
    var chapterNo: Int,
    var verseCount: Int

) : Parcelable {

    var selectedVerse: Int = 0

    constructor(source: Parcel) : this(
        source.readInt(),
        source.readInt(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readInt(),
        source.readInt()
    )


    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(_id)
        writeInt(bookId)
        writeString(bookCategory)
        writeString(bookEngName)
        writeString(bookMalName)
        writeInt(chapterNo)
        writeInt(verseCount)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ChapterDisplayEntity> = object : Parcelable.Creator<ChapterDisplayEntity> {
            override fun createFromParcel(source: Parcel): ChapterDisplayEntity = ChapterDisplayEntity(source)
            override fun newArray(size: Int): Array<ChapterDisplayEntity?> = arrayOfNulls(size)
        }
    }
}