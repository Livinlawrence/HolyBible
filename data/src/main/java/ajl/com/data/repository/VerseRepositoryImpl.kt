package ajl.com.data.repository

import ajl.com.data.db.AppDatabase
import ajl.com.data.db.VerseDao
import ajl.com.data.entity.BookmarkEntity
import ajl.com.data.entity.VerseBookmarkEntity
import ajl.com.domain.entities.ChapterDisplayEntity
import ajl.com.domain.entities.VerseDisplayEntity
import ajl.com.domain.repositories.VerseRepository
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class VerseRepositoryImpl(
    private val database: AppDatabase
) : VerseRepository {
    private val verseDao: VerseDao = database.verseDao()


    override fun getVerses(chapterDisplayEntity: ChapterDisplayEntity): MutableLiveData<List<VerseDisplayEntity>> {
        val bookEntityList = ArrayList<VerseDisplayEntity>()
        val convertedList: MutableLiveData<List<VerseDisplayEntity>> = MutableLiveData()

        GlobalScope.launch(Dispatchers.Main) {
            val verseFromDB: List<VerseBookmarkEntity> = async(Dispatchers.IO) {
                return@async verseDao.getVerseOfChapter(chapterDisplayEntity.bookId, chapterDisplayEntity.chapterNo)
            }.await()


            verseFromDB.forEach() { verseBookmarkEntity: VerseBookmarkEntity ->

                val chapter = VerseDisplayEntity(
                    false,
                    !verseBookmarkEntity.bookmarked.equals(0),
                    !verseBookmarkEntity.noted.equals(0),
                    !verseBookmarkEntity.hasFootnote.equals(0),
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
                    chapterDisplayEntity.bookEngName,
                    verseBookmarkEntity.colorTag
                )

                bookEntityList.add(chapter)
            }

            convertedList.value = bookEntityList
        }

        return convertedList
    }

    override fun searchVerse(character: String): MutableLiveData<List<VerseDisplayEntity>> {
        val bookEntityList = ArrayList<VerseDisplayEntity>()
        val convertedList: MutableLiveData<List<VerseDisplayEntity>> = MutableLiveData()

        GlobalScope.launch(Dispatchers.Main) {
            val verseFromDB: List<VerseBookmarkEntity> = async(Dispatchers.IO) {
                return@async verseDao.searchVerse(character)
            }.await()


            verseFromDB.forEach() { verseBookmarkEntity: VerseBookmarkEntity ->

                val chapter = VerseDisplayEntity(
                    false,
                    !verseBookmarkEntity.bookmarked.equals(0),
                    !verseBookmarkEntity.noted.equals(0),
                    !verseBookmarkEntity.hasFootnote.equals(0),
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
                    verseBookmarkEntity.colorTag
                )

                bookEntityList.add(chapter)
            }

            convertedList.value = bookEntityList
        }

        return convertedList
    }

    override fun insertBookmark(verseNo: Int, folderId: Int) {
        val bookmarkEntity = BookmarkEntity(
            verseNo, Date().time, folderId
        )

        GlobalScope.launch(Dispatchers.Main) {
            val verseFromDB: Long = async(Dispatchers.IO) {
                return@async verseDao.insertBookmark(bookmarkEntity)
            }.await()
        }
    }
}

