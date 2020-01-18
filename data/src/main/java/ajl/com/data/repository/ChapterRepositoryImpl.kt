package ajl.com.data.repository

import ajl.com.data.db.AppDatabase
import ajl.com.data.db.BookChapterDetailsDao
import ajl.com.data.entity.BookChapterDetailEntity
import ajl.com.domain.entities.BookDisplayEntity
import ajl.com.domain.entities.ChapterDisplayEntity
import ajl.com.domain.repositories.ChapterRepository
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ChapterRepositoryImpl(
    private val database: AppDatabase
) : ChapterRepository {
    private val bookChapterDetailsDao: BookChapterDetailsDao = database.bookChapterDetailDao()


    override fun getChapters(bookDisplayEntity: BookDisplayEntity): MutableLiveData<List<ChapterDisplayEntity>> {


        val bookEntityList = ArrayList<ChapterDisplayEntity>()
        val convertedList: MutableLiveData<List<ChapterDisplayEntity>> = MutableLiveData()

        GlobalScope.launch(Dispatchers.Main) {
            val chaptersFromDB: List<BookChapterDetailEntity> = async(Dispatchers.IO) {
                return@async bookChapterDetailsDao.getChapters(bookDisplayEntity._id)
            }.await()


            chaptersFromDB.forEach() { bookChapterDetailEntity: BookChapterDetailEntity ->

                val chapter = ChapterDisplayEntity(
                    bookChapterDetailEntity._id,
                    bookChapterDetailEntity.book_id,
                    bookDisplayEntity.bookCategory,
                    bookDisplayEntity.bookEngName,
                    bookDisplayEntity.bookMalName,
                    bookChapterDetailEntity.chapter_no,
                    bookChapterDetailEntity.verse_count
                )

                bookEntityList.add(chapter)
            }

            convertedList.value = bookEntityList
        }

        return convertedList

    }
}

