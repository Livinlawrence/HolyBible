package ajl.com.bible.utility

import ajl.com.bible.ui.footnote.FootNoteViewModel
import ajl.com.domain.usecases.GetFootNoteUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FoteNoteViewModelFactory(private val getFootNoteUseCase: GetFootNoteUseCase) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FootNoteViewModel(getFootNoteUseCase) as T
    }


    /* fun create(): BooksViewModel {
         return create(getBooksUseCase)
     }*/
    /* fun create(getBooksUseCase: GetBooksUseCase): BooksViewModel*/
}