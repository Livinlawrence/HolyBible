package ajl.com.domain.repositories

import ajl.com.domain.entities.ChapterDisplayEntity
import ajl.com.domain.entities.VerseDisplayEntity
import androidx.lifecycle.MutableLiveData


interface VerseRepository {

    fun getVerses(chapterDisplayEntity: ChapterDisplayEntity): MutableLiveData<List<VerseDisplayEntity>>

    fun searchVerse(character: String): MutableLiveData<List<VerseDisplayEntity>>

    fun insertBookmark(verseNo: Int, folderId: Int)

}