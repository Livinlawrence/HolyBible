package ajl.com.domain.entities

import android.os.Parcel
import android.os.Parcelable

data class BookDisplayEntity(
    var _id: Int,
    var chapterCount: Int,
    var bookCategory: String,
    var bookEngName: String,
    var bookMalName: String,
    var bookMalShortName: String
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readInt(),
        source.readInt(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(_id)
        writeInt(chapterCount)
        writeString(bookCategory)
        writeString(bookEngName)
        writeString(bookMalName)
        writeString(bookMalShortName)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<BookDisplayEntity> =
            object : Parcelable.Creator<BookDisplayEntity> {
                override fun createFromParcel(source: Parcel): BookDisplayEntity =
                    BookDisplayEntity(source)

                override fun newArray(size: Int): Array<BookDisplayEntity?> = arrayOfNulls(size)
            }
    }
}