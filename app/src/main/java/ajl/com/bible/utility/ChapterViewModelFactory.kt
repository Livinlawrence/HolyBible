package ajl.com.bible.utility

import ajl.com.bible.ui.chapter.ChaptersViewModel
import ajl.com.domain.usecases.GetChapterUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ChapterViewModelFactory(private val getChapterUseCase: GetChapterUseCase) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ChaptersViewModel(getChapterUseCase) as T
    }


    /* fun create(): BooksViewModel {
         return create(getBooksUseCase)
     }*/
    /* fun create(getBooksUseCase: GetBooksUseCase): BooksViewModel*/
}