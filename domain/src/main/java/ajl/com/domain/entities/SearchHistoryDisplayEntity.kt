package ajl.com.domain.entities

import android.os.Parcel
import android.os.Parcelable

data class SearchHistoryDisplayEntity(
    var _id: Int,
    var resultCount: Int,
    var searchKeyword: String,
    var date: Long
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readInt(),
        source.readInt(),
        source.readString(),
        source.readLong()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(_id)
        writeInt(resultCount)
        writeString(searchKeyword)
        writeLong(date)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<SearchHistoryDisplayEntity> =
            object : Parcelable.Creator<SearchHistoryDisplayEntity> {
                override fun createFromParcel(source: Parcel): SearchHistoryDisplayEntity =
                    SearchHistoryDisplayEntity(source)

                override fun newArray(size: Int): Array<SearchHistoryDisplayEntity?> =
                    arrayOfNulls(size)
            }
    }
}