package ajl.com.domain.repositories

import ajl.com.domain.entities.BookDisplayEntity
import ajl.com.domain.entities.ChapterDisplayEntity
import androidx.lifecycle.MutableLiveData


interface ChapterRepository {

    fun getChapters(bookDisplayEntity: BookDisplayEntity): MutableLiveData<List<ChapterDisplayEntity>>

}