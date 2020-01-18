package ajl.com.bible.utility

import ajl.com.bible.ui.book.BooksViewModel
import ajl.com.domain.usecases.GetBooksUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class BookViewModelFactory(private val getBooksUseCase: GetBooksUseCase) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return BooksViewModel(getBooksUseCase) as T
    }


    /* fun create(): BooksViewModel {
         return create(getBooksUseCase)
     }*/
    /* fun create(getBooksUseCase: GetBooksUseCase): BooksViewModel*/
}