package ajl.com.bible.ui.searchhistory.ui.searchlist

import ajl.com.bible.R
import ajl.com.bible.extensions.*
import ajl.com.bible.ui.chapter.ChaptersFragmentDirections
import ajl.com.bible.ui.home.SearchListAdapter
import ajl.com.bible.ui.verse.VerseViewModel
import ajl.com.bible.utility.IOUtility.createFileWithContent
import ajl.com.data.db.AppDatabase
import ajl.com.data.repository.VerseRepositoryImpl
import ajl.com.domain.entities.BookDisplayEntity
import ajl.com.domain.entities.SearchHistoryDisplayEntity
import ajl.com.domain.entities.VerseDisplayEntity
import ajl.com.domain.usecases.GetVerseUseCase
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_search_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class SearchListFragment :
    Fragment() {

    private lateinit var listAdapter: SearchListAdapter
    private lateinit var searchHistoryDisplayEntity: SearchHistoryDisplayEntity

    private val getVerseUseCase = GetVerseUseCase(VerseRepositoryImpl(AppDatabase(context)))

    private val verseViewModel by lazy {
        getViewModel { VerseViewModel(getVerseUseCase) }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_search_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        searchHistoryDisplayEntity = SearchListFragmentArgs.fromBundle(arguments).searchHistory


        tvSearchKeyWord.text = searchHistoryDisplayEntity.searchKeyword

        listAdapter = SearchListAdapter { verse: VerseDisplayEntity -> verseClicked(verse) }
        searchRecycler.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        searchRecycler.adapter = listAdapter

        searchBible(searchHistoryDisplayEntity.searchKeyword)

        downloadFAB.setOnClickListener {
            checkAndRequestPermissions()
        }
    }

    private fun verseClicked(verse: VerseDisplayEntity) {
        val bookDisplayEntity = BookDisplayEntity(
            verse.book_no,
            0,
            "MAL",
            verse.book,
            verse.book,
            verse.book
        )
        val action = ChaptersFragmentDirections.actionChapterToPager(bookDisplayEntity)
        action.setSelectedChapter(verse.actual_chapter_no)
        action.setSelectedVerse(verse.verse_no)
        activity?.let {
            Navigation.findNavController(it, R.id.nav_controller_fragment).navigate(action)
        }
    }

    private fun searchBible(character: String) {
        if (character.trim().isEmpty()) return
        searchRecycler.visibility = View.VISIBLE
        verseViewModel.searchVerse(character)
        verseViewModel.fetchLiveVerse().observe(this, Observer {
            searchProgressLayout.visibility = View.GONE
            it?.let {
                listAdapter.updateList(it)
            } ?: run {
                listAdapter.updateList(emptyList())
            }
        })
    }

    private fun checkAndRequestPermissions() {
        if (!isHasPermission(*initialPermissions))
            askPermission(
                permissions = *initialPermissions,
                requestCode = REQUEST_ID_MULTIPLE_PERMISSIONS
            )
        else
            writeFile()
    }

    private fun writeFile() {
        GlobalScope.launch(Dispatchers.Main) {
            async(Dispatchers.Main) {
                var stringBody = searchHistoryDisplayEntity.searchKeyword.toUpperCase() + ":-"
                for (verseDisplayEntity in listAdapter.mutableList) {
                    stringBody = stringBody + "\n " +
                            verseDisplayEntity.book + " " + verseDisplayEntity.chapter + ":" + verseDisplayEntity.verse_no + "-- " + verseDisplayEntity.verse
                }
                val savedPath = createFileWithContent(
                    getString(R.string.app_name),
                    searchHistoryDisplayEntity.searchKeyword,
                    stringBody
                )
                savedPath?.let {
                    Toast.makeText(
                        context,
                        getString(R.string.label_search_history_saved) + "to " + savedPath,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {
            handleCallPermissionResult(grantResults)
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun handleCallPermissionResult(grantResults: IntArray) {
        val denied =
            grantResults.indices.filter { grantResults[it] != PackageManager.PERMISSION_GRANTED }
        if (denied.isEmpty()) {
            permissionsGranted()
        } else {
            val readExternalFile = grantResults[0] == PackageManager.PERMISSION_GRANTED
            val writeExternalFile = grantResults[1] == PackageManager.PERMISSION_GRANTED


            if (!readExternalFile && !writeExternalFile) {
                Toast.makeText(
                    context,
                    getString(R.string.message_pls_grant_all_permissions),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun permissionsGranted() {
        writeFile()
    }
}
