package ajl.com.domain.repositories

import ajl.com.domain.entities.NoteDisplayEntity
import ajl.com.domain.entities.NoteFolderDisplayEntity
import androidx.lifecycle.MutableLiveData


interface NoteRepository {

    fun getAllNotes(noteFolderId: Int): MutableLiveData<List<NoteDisplayEntity>>

    fun insertNote(verseNo: Int, note: String, folderId: Int)
    fun updateNote(noteID: Int, note: String)

    fun deleteNote(id: Int)
    fun deleteNoteFolder(id: Int)
    fun getAllNoteFolders(): MutableLiveData<List<NoteFolderDisplayEntity>>
}