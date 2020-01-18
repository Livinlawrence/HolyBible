package ajl.com.data.repository

import ajl.com.data.db.AppDatabase
import ajl.com.data.db.BookmarkDao
import ajl.com.data.db.FolderDao
import ajl.com.data.entity.BookmarkFolderEntity
import ajl.com.data.entity.VerseBookmarkEntity
import ajl.com.domain.entities.BookmarkDisplayEntity
import ajl.com.domain.entities.BookmarkFolderDisplayEntity
import ajl.com.domain.repositories.BookMarkRepository
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class BookmarkRepositoryImpl(
    private val database: AppDatabase
) : BookMarkRepository {
    private val bookmarkDao: BookmarkDao = database.bookMarkDao()
    private val folderDao: FolderDao = database.folderDao()

    override fun getAllBookmarkFolders(): MutableLiveData<List<BookmarkFolderDisplayEntity>> {
        val convertedList: MutableLiveData<List<BookmarkFolderDisplayEntity>> = MutableLiveData()
        val bookmarkFoldersList = ArrayList<BookmarkFolderDisplayEntity>()

        GlobalScope.launch(Dispatchers.Main) {
            val bookmarkFolders: List<BookmarkFolderEntity> = async(Dispatchers.IO) {
                return@async folderDao.getAllBookmarkFolder()
            }.await()

            bookmarkFolders.forEach { bookmarkFolderEntity: BookmarkFolderEntity ->

                val bookmarkFolderDisplayEntity = BookmarkFolderDisplayEntity(
                    bookmarkFolderEntity.id,
                    bookmarkFolderEntity.foldername
                )

                bookmarkFoldersList.add(bookmarkFolderDisplayEntity)
            }

            convertedList.value = bookmarkFoldersList
        }

        return convertedList
    }

    override fun deleteBookmarkFolder(id: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            async(Dispatchers.IO) {
                return@async folderDao.deleteBookmarkFolder(id)
            }.await()
        }

        GlobalScope.launch(Dispatchers.Main) {
            async(Dispatchers.IO) {
                return@async bookmarkDao.deleteFolderBookmark(id)
            }.await()
        }
    }

    override fun getAllBookmarks(sortMode: Int, folderID: Int): MutableLiveData<List<BookmarkDisplayEntity>> {
        val bookEntityList = ArrayList<BookmarkDisplayEntity>()
        val convertedList: MutableLiveData<List<BookmarkDisplayEntity>> = MutableLiveData()

        GlobalScope.launch(Dispatchers.Main) {
            val verseFromDB: List<VerseBookmarkEntity> = async(Dispatchers.IO) {

                when (sortMode) {
                    1 -> {
                        return@async bookmarkDao.getVerseAscendingBookmarks(folderID)
                    }
                    2 -> {
                        return@async bookmarkDao.getVerseDescendingBookmarks(folderID)
                    }
                    3 -> {
                        return@async bookmarkDao.getDateAscendingBookmarks(folderID)
                    }
                    4 -> {
                        return@async bookmarkDao.getDateDescendingBookmarks(folderID)
                    }
                    else -> {
                        return@async bookmarkDao.getVerseDescendingBookmarks(folderID)
                    }

                }


            }.await()


            verseFromDB.forEach() { verseBookmarkEntity: VerseBookmarkEntity ->

                val chapter = BookmarkDisplayEntity(
                    false,
                    verseBookmarkEntity._id,
                    verseBookmarkEntity.bookmark_id,
                    verseBookmarkEntity.testament,
                    verseBookmarkEntity.book,
                    verseBookmarkEntity.book_no,
                    verseBookmarkEntity.chapter,
                    verseBookmarkEntity.actual_chapter_no,
                    verseBookmarkEntity.verse_no,
                    verseBookmarkEntity.verse,
                    verseBookmarkEntity.heading,
                    verseBookmarkEntity.book,
                    verseBookmarkEntity.foldername
                )

                bookEntityList.add(chapter)
            }

            convertedList.value = bookEntityList
        }

        return convertedList
    }


    override fun deleteBookmark(id: Int) {

        GlobalScope.launch(Dispatchers.Main) {
            async(Dispatchers.IO) {
                return@async bookmarkDao.deleteBookmark(id)
            }.await()
        }
    }
}

