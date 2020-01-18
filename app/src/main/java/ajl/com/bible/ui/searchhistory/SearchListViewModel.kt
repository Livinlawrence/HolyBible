package ajl.com.bible.ui.searchhistory

import ajl.com.domain.entities.SearchHistoryDisplayEntity
import ajl.com.domain.usecases.GetSearchHistoryUseCase
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchListViewModel(private val getSearchHistoryUseCase: GetSearchHistoryUseCase) :
    ViewModel() {


    fun getAllSearchResults(): MutableLiveData<List<SearchHistoryDisplayEntity>> {
        return getSearchHistoryUseCase.getAllSearchResults()
    }

    fun getValidSearchHistory(): MutableLiveData<List<SearchHistoryDisplayEntity>> {
        return getSearchHistoryUseCase.getValidSearchHistory()
    }


}