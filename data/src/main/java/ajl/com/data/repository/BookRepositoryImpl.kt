package ajl.com.data.repository

import ajl.com.data.db.AppDatabase
import ajl.com.data.db.BookDao
import ajl.com.data.entity.BookEntity
import ajl.com.domain.entities.BookDisplayEntity
import ajl.com.domain.repositories.BookRepository
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class BookRepositoryImpl(
    private val database: AppDatabase
) : BookRepository {
    private val bookDao: BookDao = database.bookDao()

    override fun getBooks(): MutableLiveData<List<BookDisplayEntity>> {

        val bookEntityList = ArrayList<BookDisplayEntity>()
        val convertedList: MutableLiveData<List<BookDisplayEntity>> = MutableLiveData()

        GlobalScope.launch(Dispatchers.Main) {
            val usersFromDb: List<BookEntity> = async(Dispatchers.IO) {
                return@async bookDao.getAll()
            }.await()


            usersFromDb.forEach() { bookEntity: BookEntity ->

                val bookDisplayEntity = BookDisplayEntity(
                    bookEntity._id,
                    bookEntity.chapterCount,
                    bookEntity.bookCategory,
                    bookEntity.bookEngName,
                    bookEntity.bookMalName,
                    bookEntity.bookShortName
                )

                bookEntityList.add(bookDisplayEntity)
            }

            convertedList.value = bookEntityList
        }

        return convertedList
    }

    override fun getAllNewBooks(): MutableLiveData<List<BookDisplayEntity>> {
        val bookEntityList = ArrayList<BookDisplayEntity>()
        val convertedList: MutableLiveData<List<BookDisplayEntity>> = MutableLiveData()

        GlobalScope.launch(Dispatchers.Main) {
            val usersFromDb: List<BookEntity> = async(Dispatchers.IO) {
                return@async bookDao.getAllNewBooks()
            }.await()


            usersFromDb.forEach() { bookEntity: BookEntity ->

                val bookDisplayEntity = BookDisplayEntity(
                    bookEntity._id,
                    bookEntity.chapterCount,
                    bookEntity.bookCategory,
                    bookEntity.bookEngName,
                    bookEntity.bookMalName,
                    bookEntity.bookShortName
                )

                bookEntityList.add(bookDisplayEntity)
            }

            convertedList.value = bookEntityList
        }

        return convertedList
    }

    override fun getAllOldBooks(): MutableLiveData<List<BookDisplayEntity>> {
        val bookEntityList = ArrayList<BookDisplayEntity>()
        val convertedList: MutableLiveData<List<BookDisplayEntity>> = MutableLiveData()

        GlobalScope.launch(Dispatchers.Main) {
            val usersFromDb: List<BookEntity> = async(Dispatchers.IO) {
                return@async bookDao.getAllOldBooks()
            }.await()


            usersFromDb.forEach() { bookEntity: BookEntity ->

                val bookDisplayEntity = BookDisplayEntity(
                    bookEntity._id,
                    bookEntity.chapterCount,
                    bookEntity.bookCategory,
                    bookEntity.bookEngName,
                    bookEntity.bookMalName,
                    bookEntity.bookShortName
                )

                bookEntityList.add(bookDisplayEntity)
            }

            convertedList.value = bookEntityList
        }

        return convertedList
    }
}

