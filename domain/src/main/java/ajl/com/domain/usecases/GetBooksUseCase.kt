package ajl.com.domain.usecases

import ajl.com.domain.entities.BookDisplayEntity
import ajl.com.domain.repositories.BookRepository
import androidx.lifecycle.MutableLiveData

class GetBooksUseCase(private val repository: BookRepository) {

    fun getBooks(): MutableLiveData<List<BookDisplayEntity>> {

        return repository.getBooks()
    }


    fun getAllOldBooks(): MutableLiveData<List<BookDisplayEntity>> {

        return repository.getAllOldBooks()
    }


    fun getAllNewBooks(): MutableLiveData<List<BookDisplayEntity>> {

        return repository.getAllNewBooks()
    }
}