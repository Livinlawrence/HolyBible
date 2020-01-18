package ajl.com.domain.entities

import android.os.Parcel
import android.os.Parcelable

data class BookmarkDisplayEntity(
    var selected: Boolean,
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
    var folderName: String?


) : Parcelable {
    constructor(source: Parcel) : this(
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
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt((if (selected) 1 else 0))
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
        writeString(folderName)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<BookmarkDisplayEntity> = object : Parcelable.Creator<BookmarkDisplayEntity> {
            override fun createFromParcel(source: Parcel): BookmarkDisplayEntity = BookmarkDisplayEntity(source)
            override fun newArray(size: Int): Array<BookmarkDisplayEntity?> = arrayOfNulls(size)
        }
    }
}