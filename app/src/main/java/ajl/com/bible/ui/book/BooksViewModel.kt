package ajl.com.bible.ui.book

import ajl.com.domain.entities.BookDisplayEntity
import ajl.com.domain.usecases.GetBooksUseCase
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BooksViewModel(private val getBooksUseCase: GetBooksUseCase) : ViewModel() {


    var books = MutableLiveData<List<BookDisplayEntity>>()


    fun getBooks() {
        books = getBooksUseCase.getBooks()
    }

    fun fetchLiveBooks() = books

}