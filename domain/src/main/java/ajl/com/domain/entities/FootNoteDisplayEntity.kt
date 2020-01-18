package ajl.com.domain.entities

import android.os.Parcel
import android.os.Parcelable

data class FootNoteDisplayEntity(
    var _id: Int,
    var testament: String,
    var book: String,
    var book_no: Int,
    var chapter_no: Int,
    var verse_no: Int,
    var footnote: String
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readInt(),
        source.readString(),
        source.readString(),
        source.readInt(),
        source.readInt(),
        source.readInt(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(_id)
        writeString(testament)
        writeString(book)
        writeInt(book_no)
        writeInt(chapter_no)
        writeInt(verse_no)
        writeString(footnote)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<FootNoteDisplayEntity> = object : Parcelable.Creator<FootNoteDisplayEntity> {
            override fun createFromParcel(source: Parcel): FootNoteDisplayEntity = FootNoteDisplayEntity(source)
            override fun newArray(size: Int): Array<FootNoteDisplayEntity?> = arrayOfNulls(size)
        }
    }
}