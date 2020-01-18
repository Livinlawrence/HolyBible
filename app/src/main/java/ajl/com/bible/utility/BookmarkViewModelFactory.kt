package ajl.com.bible.utility

import ajl.com.bible.ui.bookmark.BookmarkViewModel
import ajl.com.domain.usecases.GetBookmarkUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class BookmarkViewModelFactory(private val getBookmarkUseCase: GetBookmarkUseCase) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return BookmarkViewModel(getBookmarkUseCase) as T
    }


    /* fun create(): BooksViewModel {
         return create(getBooksUseCase)
     }*/
    /* fun create(getBooksUseCase: GetBooksUseCase): BooksViewModel*/
}