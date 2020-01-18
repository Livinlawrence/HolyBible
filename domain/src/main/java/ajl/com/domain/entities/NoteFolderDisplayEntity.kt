package ajl.com.domain.entities

import android.os.Parcel
import android.os.Parcelable

data class NoteFolderDisplayEntity(
    var _id: Int,
    var folderName: String
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readInt(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(_id)
        writeString(folderName)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<NoteFolderDisplayEntity> =
            object : Parcelable.Creator<NoteFolderDisplayEntity> {
                override fun createFromParcel(source: Parcel): NoteFolderDisplayEntity =
                    NoteFolderDisplayEntity(source)

                override fun newArray(size: Int): Array<NoteFolderDisplayEntity?> =
                    arrayOfNulls(size)
            }
    }
}