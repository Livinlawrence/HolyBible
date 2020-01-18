package ajl.com.bible.ui.note

import ajl.com.domain.entities.NoteDisplayEntity
import ajl.com.domain.entities.NoteFolderDisplayEntity
import ajl.com.domain.usecases.GetNoteUseCase
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NoteViewModel(private val getNoteUseCase: GetNoteUseCase) : ViewModel() {


    var notesList = MutableLiveData<List<NoteDisplayEntity>>()
    var notesFolderDisplayList = MutableLiveData<List<NoteFolderDisplayEntity>>()


    fun getAllNotes(noteFolderId: Int) {
        notesList = getNoteUseCase.getAllNotes(noteFolderId)
    }


    fun getNoteDisplayFolders() {
        notesFolderDisplayList = getNoteUseCase.getAllNoteFolders()
    }
}