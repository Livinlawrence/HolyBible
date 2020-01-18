package ajl.com.domain.usecases

import ajl.com.domain.entities.ChapterDisplayEntity
import ajl.com.domain.entities.VerseDisplayEntity
import ajl.com.domain.repositories.VerseRepository
import androidx.lifecycle.MutableLiveData

class GetVerseUseCase(private val verseRepository: VerseRepository) {

    fun getVerses(chapterDisplayEntity: ChapterDisplayEntity): MutableLiveData<List<VerseDisplayEntity>> {

        return verseRepository.getVerses(chapterDisplayEntity)
    }

    fun searchVerse(character: String): MutableLiveData<List<VerseDisplayEntity>> {

        return verseRepository.searchVerse(character)
    }

    fun insertBookmark(verseNo: Int, folderId: Int) {

        return verseRepository.insertBookmark(verseNo, folderId)
    }

}