package ajl.com.bible.ui.chapter

import ajl.com.domain.entities.BookDisplayEntity
import ajl.com.domain.entities.ChapterDisplayEntity
import ajl.com.domain.usecases.GetChapterUseCase
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ChaptersViewModel(private val getChapterUseCase: GetChapterUseCase) : ViewModel() {


    var chaptersList = MutableLiveData<List<ChapterDisplayEntity>>()


    fun fetchChapters(bookDisplayEntity: BookDisplayEntity) {
        chaptersList = getChapterUseCase.getChapters(bookDisplayEntity)
    }

    fun fetchLiveChapters() = chaptersList
}