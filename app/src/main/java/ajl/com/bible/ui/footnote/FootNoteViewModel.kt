package ajl.com.bible.ui.footnote

import ajl.com.domain.entities.FootNoteDisplayEntity
import ajl.com.domain.usecases.GetFootNoteUseCase
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FootNoteViewModel(private val getFootNoteUseCase: GetFootNoteUseCase) : ViewModel() {


    var footNoteList = MutableLiveData<List<FootNoteDisplayEntity>>()


    fun getFootNotes() {
        footNoteList = getFootNoteUseCase.getFoteNotes()
    }

    fun fetchLiveFoteNotes() = footNoteList
}