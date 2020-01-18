package ajl.com.domain.repositories

import ajl.com.domain.entities.SearchHistoryDisplayEntity
import androidx.lifecycle.MutableLiveData


interface SearchHistoryRepository {

    fun getAllSearchResults(): MutableLiveData<List<SearchHistoryDisplayEntity>>

    fun getValidSearchHistory(): MutableLiveData<List<SearchHistoryDisplayEntity>>

    fun deleteSearchRecord(id: Int)

    fun insertSearchRecord(resultCount: Int, keyword: String)

}