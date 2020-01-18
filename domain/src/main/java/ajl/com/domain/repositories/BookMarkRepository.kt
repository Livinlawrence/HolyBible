package ajl.com.domain.repositories

import ajl.com.domain.entities.BookmarkDisplayEntity
import ajl.com.domain.entities.BookmarkFolderDisplayEntity
import androidx.lifecycle.MutableLiveData


interface BookMarkRepository {
    fun getAllBookmarks(sortMode: Int, folderID: Int): MutableLiveData<List<BookmarkDisplayEntity>>
    fun deleteBookmark(id: Int)
    fun deleteBookmarkFolder(id: Int)
    fun getAllBookmarkFolders(): MutableLiveData<List<BookmarkFolderDisplayEntity>>
}