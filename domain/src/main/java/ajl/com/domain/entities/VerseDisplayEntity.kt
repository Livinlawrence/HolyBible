package ajl.com.domain.entities

import android.os.Parcel
import android.os.Parcelable

data class VerseDisplayEntity(
    var selected: Boolean,
    var bookMarked: Boolean,
    var noted: Boolean,
    var hasFootnote: Boolean,
    var _id: Int,
    var bookmark_id: Int,
    var testament: String,
    var book: String,
    var book_no: Int,
    var chapter: Int,
    var actual_chapter_no: Int,
    var verse_no: Int,
    var verse: String,
    var heading: String,
    var bookNameEng: String,
    var colorTag: Int?

) : Parcelable {
    constructor(source: Parcel) : this(
        1 == source.readInt(),
        1 == source.readInt(),
        1 == source.readInt(),
        1 == source.readInt(),
        source.readInt(),
        source.readInt(),
        source.readString(),
        source.readString(),
        source.readInt(),
        source.readInt(),
        source.readInt(),
        source.readInt(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readValue(Int::class.java.classLoader) as Int?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt((if (selected) 1 else 0))
        writeInt((if (bookMarked) 1 else 0))
        writeInt((if (noted) 1 else 0))
        writeInt((if (hasFootnote) 1 else 0))
        writeInt(_id)
        writeInt(bookmark_id)
        writeString(testament)
        writeString(book)
        writeInt(book_no)
        writeInt(chapter)
        writeInt(actual_chapter_no)
        writeInt(verse_no)
        writeString(verse)
        writeString(heading)
        writeString(bookNameEng)
        writeValue(colorTag)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<VerseDisplayEntity> = object : Parcelable.Creator<VerseDisplayEntity> {
            override fun createFromParcel(source: Parcel): VerseDisplayEntity = VerseDisplayEntity(source)
            override fun newArray(size: Int): Array<VerseDisplayEntity?> = arrayOfNulls(size)
        }
    }
}