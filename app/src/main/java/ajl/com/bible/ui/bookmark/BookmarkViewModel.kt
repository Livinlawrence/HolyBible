package ajl.com.bible.ui.bookmark

import ajl.com.domain.entities.BookmarkDisplayEntity
import ajl.com.domain.entities.BookmarkFolderDisplayEntity
import ajl.com.domain.usecases.GetBookmarkUseCase
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BookmarkViewModel(private val getBookmarkUseCase: GetBookmarkUseCase) : ViewModel() {


    private var bookmarkList = MutableLiveData<List<BookmarkDisplayEntity>>()
    var bookmarkFoldersList = MutableLiveData<List<BookmarkFolderDisplayEntity>>()


    fun getAllBookmarks(sortMode: Int, folderID: Int) {
        bookmarkList = getBookmarkUseCase.getAllBookmarks(sortMode, folderID)
    }

    fun getAllBookmarkFolders() {
        bookmarkFoldersList = getBookmarkUseCase.getAllBookmarkFolders()
    }


    fun fetchLiveBookmarks() = bookmarkList
}