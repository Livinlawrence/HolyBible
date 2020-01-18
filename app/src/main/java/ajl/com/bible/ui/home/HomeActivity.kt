package ajl.com.bible.ui.home

import ajl.com.bible.R
import ajl.com.bible.R.id.action_search
import ajl.com.bible.controllers.OnFragmentInteractionListener
import ajl.com.bible.extensions.getViewModel
import ajl.com.bible.extensions.hideKeyboard
import ajl.com.bible.extensions.showGoogleIndicKeyboardPlayStoreApp
import ajl.com.bible.ui.book.BooksFragmentDirections
import ajl.com.bible.ui.chapter.ChaptersFragmentDirections
import ajl.com.bible.ui.searchhistory.RecentSearchListAdapter
import ajl.com.bible.ui.searchhistory.SearchListViewModel
import ajl.com.bible.ui.verse.VerseViewModel
import ajl.com.bible.utility.Constants
import ajl.com.data.db.AppDatabase
import ajl.com.data.repository.SearchHistoryRepositoryImpl
import ajl.com.data.repository.VerseRepositoryImpl
import ajl.com.domain.entities.BookDisplayEntity
import ajl.com.domain.entities.SearchHistoryDisplayEntity
import ajl.com.domain.entities.VerseDisplayEntity
import ajl.com.domain.usecases.GetSearchHistoryUseCase
import ajl.com.domain.usecases.GetVerseUseCase
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.core.view.MenuItemCompat
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.layout_navigation.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class HomeActivity : AppCompatActivity(), OnFragmentInteractionListener, View.OnClickListener {
    private lateinit var navController: NavController

    private lateinit var listAdapter: SearchListAdapter
    private lateinit var recentSearchListAdapter: RecentSearchListAdapter
    var searchRedirection: Boolean = false

    private val getVerseUseCase = GetVerseUseCase(VerseRepositoryImpl(AppDatabase(this)))

    private val searchHistoryUseCase =
        GetSearchHistoryUseCase(SearchHistoryRepositoryImpl(AppDatabase(this)))

    private val searchListViewModel by lazy {
        getViewModel { SearchListViewModel(searchHistoryUseCase) }
    }

    private val verseViewModel by lazy {
        getViewModel { VerseViewModel(getVerseUseCase) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            ajl.com.bible.R.string.navigation_drawer_open,
            ajl.com.bible.R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()


        navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        NavigationUI.setupWithNavController(bottomNav, navController)

        installKeyboard.setOnClickListener {
            showGoogleIndicKeyboardPlayStoreApp()
        }

        listAdapter = SearchListAdapter { verse: VerseDisplayEntity -> verseClicked(verse) }
        recentSearchListAdapter =
            RecentSearchListAdapter { searchHistoryDisplayEntity: SearchHistoryDisplayEntity ->
                searchHistoryClicked(
                    searchHistoryDisplayEntity
                )
            }

        searchRecycler.layoutManager =
            LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)

        tvHistory.setOnClickListener {
            showRecentSearchList()
        }

        layoutBookmarks.setOnClickListener(this)
        layoutNotes.setOnClickListener(this)
        layoutSettings.setOnClickListener(this)
        layoutAbout.setOnClickListener(this)
        layoutShare.setOnClickListener(this)
        layoutRate.setOnClickListener(this)
        layoutSearchHistory.setOnClickListener(this)


        // Initialize the action bar drawer toggle instance
        val drawerToggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        ) {
            override fun onDrawerClosed(view: View) {
                super.onDrawerClosed(view)
                //toast("Drawer closed")
            }

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                GlobalScope.launch(Dispatchers.Main) {
                    val bookmarkCount: Int = async(Dispatchers.IO) {
                        return@async AppDatabase(applicationContext).bookMarkDao()
                            .getNumberOfBookmarks()
                    }.await()
                    if (bookmarkCount > 99) bookMarkCount.text = "99+"
                    else bookMarkCount.text = bookmarkCount.toString()


                    val note: Int = async(Dispatchers.IO) {
                        return@async AppDatabase(applicationContext).noteDao().getNumberOfNotes()
                    }.await()
                    if (bookmarkCount > 99) noteCount.text = "99+"
                    else noteCount.text = note.toString()


                    val searchHistoryCount: Int = async(Dispatchers.IO) {
                        return@async AppDatabase(applicationContext).searchHistoryDao()
                            .getNumberOfSearches()
                    }.await()
                    if (searchHistoryCount > 99) searchCount.text = "99+"
                    else searchCount.text = searchHistoryCount.toString()
                }
            }
        }


        // Configure the drawer layout to add listener and show icon on toolbar
        drawerToggle.isDrawerIndicatorEnabled = true
        drawer_layout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
    }

    private fun searchHistoryClicked(searchHistoryDisplayEntity: SearchHistoryDisplayEntity) {
        hideKeyboard()
        searchBible(searchHistoryDisplayEntity.searchKeyword, true)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu!!.findItem(action_search).actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {


            override fun onQueryTextChange(character: String): Boolean {
                Log.e("SEARCH ", character)
                searchBible(character, false)
                return false
            }

            override fun onQueryTextSubmit(character: String): Boolean {
                Log.e("SEARCH SUBMITTED ", character)
                searchBible(character, true)
                return false
            }

        })

        MenuItemCompat.setOnActionExpandListener(
            menu.findItem(action_search),
            object : MenuItemCompat.OnActionExpandListener {
                override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                    Log.e("SEARCH ", "CLICKED")
                    searchFrame.visibility = View.VISIBLE
                    bottomNav.visibility = View.GONE
                    showRecentSearchList()
                    return true
                }

                override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                    Log.e("SEARCH ", "CLOSED")
                    hideSearchView()
                    return true
                }
            })
        searchView.setSearchableInfo(
            searchManager.getSearchableInfo(componentName)
        )
        return super.onCreateOptionsMenu(menu)
    }

    private fun showRecentSearchList() {
        searchListViewModel.getAllSearchResults().observe(this, Observer {
            it?.let {
                swipeRefreshLayout.isRefreshing = false
                noResultsLayout.visibility = View.GONE
                searchRecycler.visibility = View.VISIBLE
                keyboardLayout.visibility = View.GONE
                recentSearchListAdapter.updateList(it)
                searchRecycler.adapter = recentSearchListAdapter


                if (it.isEmpty()) {
                    keyboardLayout.visibility = View.VISIBLE
                }
            } ?: run {
                keyboardLayout.visibility = View.VISIBLE
            }
        })
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (searchRedirection) {
            MenuItemCompat.collapseActionView(menu!!.findItem(action_search))
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun closeOptionsMenu() {
        Log.e("SEARCH ", "CLOSED")
        hideSearchView()
        super.closeOptionsMenu()
    }

    fun searchBible(character: String, isSubmitted: Boolean) {
        swipeRefreshLayout.isRefreshing = true

        if (character.trim().isEmpty()) return

        verseViewModel.searchVerse(character)
        verseViewModel.fetchLiveVerse().observe(this, Observer {
            it?.let {
                Log.e("SEARCH ", "Results ")

                //save history
                if (isSubmitted) {
                    searchHistoryUseCase.insertSearchRecord(listAdapter.mutableList.size, character)
                }
                swipeRefreshLayout.isRefreshing = false
                if (it.isEmpty()) {
                    Log.e("SEARCH ", "isEmpty ")
                    listAdapter.updateList(emptyList())
                    noResultsLayout.visibility = View.VISIBLE
                } else {
                    searchRecycler.visibility = View.VISIBLE
                    noResultsLayout.visibility = View.GONE
                    listAdapter.updateList(it)
                }


            } ?: run {
                listAdapter.updateList(emptyList())
                noResultsLayout.visibility = View.VISIBLE
            }
            searchRecycler.adapter = listAdapter
        })

    }

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.layoutBookmarks -> {
                setTitle(R.string.label_bookmarks)
                navController.navigate(R.id.navigation_bookmark_folder_list)
            }

            R.id.layoutNotes -> {
                setTitle(R.string.label_notes)
                navController.navigate(R.id.noteFoldersFragment)
            }

            R.id.layoutSettings -> {
                navController.navigate(R.id.settingsDialogFragment)
                setTitle(R.string.label_settings)
            }

            R.id.layoutAbout -> {
                navController.navigate(R.id.aboutDialogFragment)
                setTitle(R.string.label_about)
            }

            R.id.layoutShare -> {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, getString(R.string.app_share_text))
                    type = "text/plain"
                }
                // startActivity(sendIntent)
            }


            R.id.layoutRate -> {
                // showPlayStoreApp()
            }

            R.id.layoutSearchHistory -> {
                setTitle(R.string.label_search_history)
                navController.navigate(R.id.searchHistoryListFragment)
            }

            else -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
    }


    private fun verseClicked(verseDisplayEntity: VerseDisplayEntity) {
        hideSearchView()
        val bookDisplayEntity = BookDisplayEntity(
            verseDisplayEntity.book_no,
            0,
            "MAL",
            verseDisplayEntity.book,
            verseDisplayEntity.book,
            verseDisplayEntity.book
        )
        val action = ChaptersFragmentDirections.actionChapterToPager(bookDisplayEntity)
        action.setSelectedChapter(verseDisplayEntity.actual_chapter_no)
        action.setSelectedVerse(verseDisplayEntity.verse_no)
        let { Navigation.findNavController(it, R.id.nav_host_fragment).navigate(action) }
    }

    fun hideSearchView() {
        Log.e("SEARCH ", "Hide View ")
        searchRecycler.visibility = View.GONE
        searchFrame.visibility = View.GONE
        swipeRefreshLayout.isRefreshing = false
        noResultsLayout.visibility = View.GONE
        bottomNav.visibility = View.VISIBLE
        listAdapter.updateList(emptyList())
    }

    private fun showSearchView() {
        searchRecycler.visibility = View.VISIBLE
        searchFrame.visibility = View.VISIBLE
        // bottomNav.visibility = View.GONE
        supportInvalidateOptionsMenu()
    }


    override fun onFragmentInteraction(mode: Int, dat: Any) {
        when (mode) {
            Constants.REDIRECT_CHAPTERS -> {
                val action = BooksFragmentDirections.actionBookToChapters(dat as BookDisplayEntity)
                action.setBook(dat)
                navController.navigate(action)

            }
        }
    }

    override fun setTitle(title: String) {}

    override fun onBackPressed() {
        if (searchRedirection) {
            showSearchView()
            setTitle("Search Results")
            searchRedirection = false
        }

        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}

