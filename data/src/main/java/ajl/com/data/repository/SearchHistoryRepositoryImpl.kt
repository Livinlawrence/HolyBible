package ajl.com.data.repository

import ajl.com.data.db.AppDatabase
import ajl.com.data.db.SearchHistoryDao
import ajl.com.data.entity.SearchHistoryEntity
import ajl.com.domain.entities.SearchHistoryDisplayEntity
import ajl.com.domain.repositories.SearchHistoryRepository
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class SearchHistoryRepositoryImpl(
    private val database: AppDatabase
) : SearchHistoryRepository {
    private val searchHistoryDao: SearchHistoryDao = database.searchHistoryDao()

    override fun getAllSearchResults(): MutableLiveData<List<SearchHistoryDisplayEntity>> {
        val entityList = ArrayList<SearchHistoryDisplayEntity>()
        val convertedList: MutableLiveData<List<SearchHistoryDisplayEntity>> = MutableLiveData()

        GlobalScope.launch(Dispatchers.Main) {
            val usersFromDb: List<SearchHistoryEntity> = async(Dispatchers.IO) {
                return@async searchHistoryDao.getAllSearchHistory()
            }.await()


            usersFromDb.forEach() { searchHistoryEntity: SearchHistoryEntity ->

                val bookDisplayEntity = SearchHistoryDisplayEntity(
                    searchHistoryEntity._id,
                    searchHistoryEntity.resultNo,
                    searchHistoryEntity.keyword,
                    searchHistoryEntity.dateInMillis
                )

                entityList.add(bookDisplayEntity)
            }

            convertedList.value = entityList
        }

        return convertedList
    }

    override fun getValidSearchHistory(): MutableLiveData<List<SearchHistoryDisplayEntity>> {
        val entityList = ArrayList<SearchHistoryDisplayEntity>()
        val convertedList: MutableLiveData<List<SearchHistoryDisplayEntity>> = MutableLiveData()

        GlobalScope.launch(Dispatchers.Main) {
            val usersFromDb: List<SearchHistoryEntity> = async(Dispatchers.IO) {
                return@async searchHistoryDao.getValidSearchHistory()
            }.await()


            usersFromDb.forEach() { searchHistoryEntity: SearchHistoryEntity ->

                val bookDisplayEntity = SearchHistoryDisplayEntity(
                    searchHistoryEntity._id,
                    searchHistoryEntity.resultNo,
                    searchHistoryEntity.keyword,
                    searchHistoryEntity.dateInMillis
                )

                entityList.add(bookDisplayEntity)
            }

            convertedList.value = entityList
        }

        return convertedList
    }

    override fun deleteSearchRecord(id: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            async(Dispatchers.IO) {
                return@async searchHistoryDao.deleteSearch(id)
            }.await()
        }
    }

    override fun insertSearchRecord(resultCount: Int, keyword: String) {
        val searchHistoryEntity = SearchHistoryEntity(
            keyword, Date().time, resultCount
        )

        GlobalScope.launch(Dispatchers.Main) {
            val insertID: Long = async(Dispatchers.IO) {
                return@async searchHistoryDao.insert(searchHistoryEntity)
            }.await()
        }
    }
}

