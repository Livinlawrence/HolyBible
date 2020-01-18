package ajl.com.bible.ui.verse

import ajl.com.domain.entities.ChapterDisplayEntity
import ajl.com.domain.entities.VerseDisplayEntity
import ajl.com.domain.usecases.GetVerseUseCase
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class VerseViewModel(private val getVerseUseCase: GetVerseUseCase) : ViewModel() {


    var verseList = MutableLiveData<List<VerseDisplayEntity>>()


    fun fetchVerse(chapterDisplayEntity: ChapterDisplayEntity) {
        verseList = getVerseUseCase.getVerses(chapterDisplayEntity)
    }

    fun searchVerse(character: String) {
        verseList = getVerseUseCase.searchVerse(character)
    }

    fun fetchLiveVerse() = verseList

}