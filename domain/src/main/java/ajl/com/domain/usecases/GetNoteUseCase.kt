package ajl.com.domain.usecases

import ajl.com.domain.entities.NoteDisplayEntity
import ajl.com.domain.entities.NoteFolderDisplayEntity
import ajl.com.domain.repositories.NoteRepository
import androidx.lifecycle.MutableLiveData

class GetNoteUseCase(private val noteRepository: NoteRepository) {

    fun getAllNotes(noteFolderId: Int): MutableLiveData<List<NoteDisplayEntity>> {

        return noteRepository.getAllNotes(noteFolderId)
    }

    fun insertNote(verseNo: Int, note: String, folderId: Int) {

        return noteRepository.insertNote(verseNo, note, folderId)
    }


    fun updateNote(noteID: Int, note: String) {

        return noteRepository.updateNote(noteID, note)
    }


    fun deleteBookmark(id: Int) {
        return noteRepository.deleteNote(id)
    }


    fun deleteNoteFolder(id: Int) {
        return noteRepository.deleteNoteFolder(id)
    }

    fun getAllNoteFolders(): MutableLiveData<List<NoteFolderDisplayEntity>> {
        return noteRepository.getAllNoteFolders()
    }
}