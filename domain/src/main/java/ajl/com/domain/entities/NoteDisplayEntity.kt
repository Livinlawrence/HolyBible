package ajl.com.domain.entities

import android.os.Parcel
import android.os.Parcelable

data class NoteDisplayEntity(
    var selected: Boolean,
    var verse_id: Int,
    var note_id: Int,
    var testament: String,
    var book: String,
    var book_no: Int,
    var chapter: Int,
    var actual_chapter_no: Int,
    var verse_no: Int,
    var verse: String,
    var heading: String,
    var bookNameEng: String,
    var note: String,
    var foldername: String

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
        source.readString(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt((if (selected) 1 else 0))
        writeInt(verse_id)
        writeInt(note_id)
        writeString(testament)
        writeString(book)
        writeInt(book_no)
        writeInt(chapter)
        writeInt(actual_chapter_no)
        writeInt(verse_no)
        writeString(verse)
        writeString(heading)
        writeString(bookNameEng)
        writeString(note)
        writeString(foldername)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<NoteDisplayEntity> = object : Parcelable.Creator<NoteDisplayEntity> {
            override fun createFromParcel(source: Parcel): NoteDisplayEntity = NoteDisplayEntity(source)
            override fun newArray(size: Int): Array<NoteDisplayEntity?> = arrayOfNulls(size)
        }
    }
}