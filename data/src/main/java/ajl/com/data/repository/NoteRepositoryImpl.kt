package ajl.com.data.repository

import ajl.com.data.db.AppDatabase
import ajl.com.data.db.FolderDao
import ajl.com.data.entity.NoteEntity
import ajl.com.data.entity.NotesFolderEntity
import ajl.com.data.entity.VerseNoteEntity
import ajl.com.domain.entities.NoteDisplayEntity
import ajl.com.domain.entities.NoteFolderDisplayEntity
import ajl.com.domain.repositories.NoteRepository
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class NoteRepositoryImpl(
    private val database: AppDatabase
) : NoteRepository {
    private val noteDao = database.noteDao()
    private val folderDao: FolderDao = database.folderDao()


    override fun getAllNoteFolders(): MutableLiveData<List<NoteFolderDisplayEntity>> {
        val convertedList: MutableLiveData<List<NoteFolderDisplayEntity>> = MutableLiveData()
        val noteDisplayFoldersList = ArrayList<NoteFolderDisplayEntity>()

        GlobalScope.launch(Dispatchers.Main) {
            val noteFolders: List<NotesFolderEntity> = async(Dispatchers.IO) {
                return@async folderDao.getAllNotesFolder()
            }.await()

            noteFolders.forEach { notesFolderEntity: NotesFolderEntity ->

                val bookmarkFolderDisplayEntity = NoteFolderDisplayEntity(
                    notesFolderEntity.id,
                    notesFolderEntity.foldername
                )

                noteDisplayFoldersList.add(bookmarkFolderDisplayEntity)
            }

            convertedList.value = noteDisplayFoldersList
        }

        return convertedList
    }

    override fun deleteNoteFolder(id: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            async(Dispatchers.IO) {
                return@async folderDao.deleteNoteFolder(id)
            }.await()
        }

        GlobalScope.launch(Dispatchers.Main) {
            async(Dispatchers.IO) {
                return@async noteDao.deleteFolderNote(id)
            }.await()
        }
    }

    override fun getAllNotes(noteFolderId: Int): MutableLiveData<List<NoteDisplayEntity>> {
        val bookEntityList = ArrayList<NoteDisplayEntity>()
        val convertedList: MutableLiveData<List<NoteDisplayEntity>> = MutableLiveData()

        GlobalScope.launch(Dispatchers.Main) {
            val verseFromDB: List<VerseNoteEntity> = async(Dispatchers.IO) {
                return@async noteDao.getAllNotes(noteFolderId)
            }.await()


            verseFromDB.forEach() { verseNoteEntity: VerseNoteEntity ->

                val noteDisplayEntity = NoteDisplayEntity(
                    false,
                    verseNoteEntity.verse_id,
                    verseNoteEntity.note_id,
                    verseNoteEntity.testament,
                    verseNoteEntity.book,
                    verseNoteEntity.book_no,
                    verseNoteEntity.chapter,
                    verseNoteEntity.actual_chapter_no,
                    verseNoteEntity.verse_no,
                    verseNoteEntity.verse,
                    verseNoteEntity.heading,
                    verseNoteEntity.book,
                    verseNoteEntity.note,
                    verseNoteEntity.foldername
                )

                bookEntityList.add(noteDisplayEntity)
            }

            convertedList.value = bookEntityList
        }

        return convertedList
    }

    override fun insertNote(verseNo: Int, note: String, folderId: Int) {
        val noteEntity = NoteEntity(
            verseNo, note, folderId
        )

        GlobalScope.launch(Dispatchers.Main) {
            val verseFromDB: Long = async(Dispatchers.IO) {
                return@async noteDao.insert(noteEntity)
            }.await()
        }
    }


    override fun updateNote(id: Int, note: String) {
        GlobalScope.launch(Dispatchers.Main) {
            async(Dispatchers.IO) {
                return@async noteDao.updateNote(id, note)
            }.await()
        }
    }


    override fun deleteNote(id: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            async(Dispatchers.IO) {
                return@async noteDao.deleteNote(id)
            }.await()
        }

    }
}

