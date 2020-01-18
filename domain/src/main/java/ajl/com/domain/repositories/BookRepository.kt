package ajl.com.domain.repositories

import ajl.com.domain.entities.BookDisplayEntity
import androidx.lifecycle.MutableLiveData


interface BookRepository {

    fun getBooks(): MutableLiveData<List<BookDisplayEntity>>
    fun getAllOldBooks(): MutableLiveData<List<BookDisplayEntity>>
    fun getAllNewBooks(): MutableLiveData<List<BookDisplayEntity>>

}