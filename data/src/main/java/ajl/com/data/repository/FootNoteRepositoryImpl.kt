package ajl.com.data.repository

import ajl.com.data.db.AppDatabase
import ajl.com.data.db.FoteNoteDao
import ajl.com.data.entity.FootnoteEntity
import ajl.com.domain.entities.FootNoteDisplayEntity
import ajl.com.domain.repositories.FootNoteRepository
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FootNoteRepositoryImpl(
    private val database: AppDatabase
) : FootNoteRepository {
    private val foteNoteDao: FoteNoteDao = database.footNoteDao()


    override fun getFootNotes(): MutableLiveData<List<FootNoteDisplayEntity>> {
        val bookEntityList = ArrayList<FootNoteDisplayEntity>()
        val convertedList: MutableLiveData<List<FootNoteDisplayEntity>> = MutableLiveData()

        GlobalScope.launch(Dispatchers.Main) {
            val verseFromDB: List<FootnoteEntity> = async(Dispatchers.IO) {
                return@async foteNoteDao.getAll()
            }.await()


            verseFromDB.forEach() { verseBookmarkEntity: FootnoteEntity ->

                val chapter = FootNoteDisplayEntity(
                    verseBookmarkEntity._id,
                    verseBookmarkEntity.testament,
                    verseBookmarkEntity.book,
                    verseBookmarkEntity.book_no,
                    verseBookmarkEntity.chapter_no,
                    verseBookmarkEntity.verse_no,
                    verseBookmarkEntity.fote_note
                )

                bookEntityList.add(chapter)
            }
            convertedList.value = bookEntityList
        }

        return convertedList
    }
}

