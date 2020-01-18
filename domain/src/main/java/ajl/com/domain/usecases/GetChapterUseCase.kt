package ajl.com.domain.usecases

import ajl.com.domain.entities.BookDisplayEntity
import ajl.com.domain.entities.ChapterDisplayEntity
import ajl.com.domain.repositories.ChapterRepository
import androidx.lifecycle.MutableLiveData

class GetChapterUseCase(private val repository: ChapterRepository) {

    fun getChapters(bookDisplayEntity: BookDisplayEntity): MutableLiveData<List<ChapterDisplayEntity>> {

        return repository.getChapters(bookDisplayEntity)
    }


}