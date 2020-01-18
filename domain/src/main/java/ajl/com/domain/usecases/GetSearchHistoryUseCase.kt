package ajl.com.domain.usecases

import ajl.com.domain.entities.SearchHistoryDisplayEntity
import ajl.com.domain.repositories.SearchHistoryRepository
import androidx.lifecycle.MutableLiveData

class GetSearchHistoryUseCase(private val searchHistoryRepository: SearchHistoryRepository) {

    fun insertSearchRecord(resultCount: Int, keyword: String) {
        return searchHistoryRepository.insertSearchRecord(resultCount, keyword)
    }

    fun getAllSearchResults(): MutableLiveData<List<SearchHistoryDisplayEntity>> {
        return searchHistoryRepository.getAllSearchResults()
    }

    fun getValidSearchHistory(): MutableLiveData<List<SearchHistoryDisplayEntity>> {
        return searchHistoryRepository.getValidSearchHistory()
    }

    fun deleteSearchRecord(id: Int) {
        return searchHistoryRepository.deleteSearchRecord(id)
    }
}