package ajl.com.domain.usecases

import ajl.com.domain.entities.BookmarkDisplayEntity
import ajl.com.domain.entities.BookmarkFolderDisplayEntity
import ajl.com.domain.repositories.BookMarkRepository
import androidx.lifecycle.MutableLiveData

class GetBookmarkUseCase(private val bookMarkRepository: BookMarkRepository) {

    fun getAllBookmarks(sortMode: Int, folderID: Int): MutableLiveData<List<BookmarkDisplayEntity>> {
        return bookMarkRepository.getAllBookmarks(sortMode, folderID)
    }

    fun getAllBookmarkFolders(): MutableLiveData<List<BookmarkFolderDisplayEntity>> {
        return bookMarkRepository.getAllBookmarkFolders()
    }

    fun deleteBookmark(id: Int) {
        return bookMarkRepository.deleteBookmark(id)
    }

    fun deleteBookmarkFolder(id: Int) {
        return bookMarkRepository.deleteBookmarkFolder(id)
    }
}