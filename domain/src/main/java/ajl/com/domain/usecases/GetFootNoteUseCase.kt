package ajl.com.domain.usecases

import ajl.com.domain.entities.FootNoteDisplayEntity
import ajl.com.domain.repositories.FootNoteRepository
import androidx.lifecycle.MutableLiveData

class GetFootNoteUseCase(private val footNoteRepository: FootNoteRepository) {

    fun getFoteNotes(): MutableLiveData<List<FootNoteDisplayEntity>> {

        return footNoteRepository.getFootNotes()
    }

}