package ajl.com.bible.utility

import ajl.com.bible.ui.verse.VerseViewModel
import ajl.com.domain.usecases.GetVerseUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class VerseViewModelFactory(private val getVerseUseCase: GetVerseUseCase) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return VerseViewModel(getVerseUseCase) as T
    }


    /* fun create(): BooksViewModel {
         return create(getBooksUseCase)
     }*/
    /* fun create(getBooksUseCase: GetBooksUseCase): BooksViewModel*/
}