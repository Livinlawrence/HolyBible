package ajl.com.bible.ui.searchhistory

import ajl.com.bible.R
import ajl.com.bible.controllers.OnFragmentInteractionListener
import ajl.com.bible.extensions.getViewModel
import ajl.com.bible.extensions.launchActivity
import ajl.com.data.db.AppDatabase
import ajl.com.data.repository.SearchHistoryRepositoryImpl
import ajl.com.domain.entities.SearchHistoryDisplayEntity
import ajl.com.domain.usecases.GetSearchHistoryUseCase
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_bookmarks.recyclerBooks
import kotlinx.android.synthetic.main.fragment_search_history.*


class SearchHistoryListFragment : Fragment() {


    private lateinit var listAdapter: SearchHistoryListAdapter
    private var listener: OnFragmentInteractionListener? = null
    private var folderID = 0
    val searchHistoryUseCase =
        GetSearchHistoryUseCase(SearchHistoryRepositoryImpl(AppDatabase(context)))

    private val searchListViewModel by lazy {
        getViewModel { SearchListViewModel(searchHistoryUseCase) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_history, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = getString(R.string.label_search_history)
        listAdapter =
            SearchHistoryListAdapter { searchHistoryDisplayEntity: SearchHistoryDisplayEntity ->
                searchHistoryClicked(
                    searchHistoryDisplayEntity
                )
            }

        recyclerBooks.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerBooks.adapter = listAdapter


        listAdapter.setOnItemClickListener(object : SearchHistoryListAdapter.ItemClickListener {

            override fun onDelete(
                searchHistoryDisplayEntity: SearchHistoryDisplayEntity,
                position: Int
            ) {
                searchHistoryUseCase.deleteSearchRecord(searchHistoryDisplayEntity._id)
                listAdapter.searchHistoryList.removeAt(position)
                listAdapter.notifyItemRemoved(position)
            }
        })
    }

    private fun searchHistoryClicked(bookmarkDisplayEntity: SearchHistoryDisplayEntity) {
        launchActivity<SearchListActivity> {
            putExtra("SearchHistoryDisplayEntity", bookmarkDisplayEntity)
        }
    }

    override fun onStart() {
        super.onStart()
        searchListViewModel.getValidSearchHistory().observe(this, Observer {
            searchProgressLayout.visibility = View.GONE
            listAdapter.updateList(it ?: emptyList())
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}
