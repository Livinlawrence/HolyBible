package ajl.com.domain.repositories

import ajl.com.domain.entities.FootNoteDisplayEntity
import androidx.lifecycle.MutableLiveData


interface FootNoteRepository {

    fun getFootNotes(): MutableLiveData<List<FootNoteDisplayEntity>>

}