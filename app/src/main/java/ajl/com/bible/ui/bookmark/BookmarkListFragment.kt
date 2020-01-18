package ajl.com.bible.ui.bookmark

import ajl.com.bible.R
import ajl.com.bible.controllers.OnFragmentInteractionListener
import ajl.com.bible.ui.chapter.ChaptersFragmentDirections
import ajl.com.bible.utility.BookmarkViewModelFactory
import ajl.com.bible.utility.Constants
import ajl.com.data.db.AppDatabase
import ajl.com.data.entity.BookmarkFolderEntity
import ajl.com.data.repository.BookmarkRepositoryImpl
import ajl.com.domain.entities.BookDisplayEntity
import ajl.com.domain.entities.BookmarkDisplayEntity
import ajl.com.domain.entities.BookmarkFolderDisplayEntity
import ajl.com.domain.usecases.GetBookmarkUseCase
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_bookmarks.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class BookmarkListFragment : Fragment() {

    private lateinit var bookmarkViewModel: BookmarkViewModel
    private lateinit var listAdapter: BookmarkListAdapter
    private var listener: OnFragmentInteractionListener? = null
    private var folderID = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bookmarks, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val bookmarkFolder: BookmarkFolderDisplayEntity =
            BookmarkListFragmentArgs.fromBundle(arguments).bookmarkFolderName
        folderID = bookmarkFolder._id
        activity?.title = getString(R.string.label_bookmarks) + " of " + bookmarkFolder.folderName

        val getBookmarkUseCase = GetBookmarkUseCase(BookmarkRepositoryImpl(AppDatabase(context)))

        val factory = BookmarkViewModelFactory(getBookmarkUseCase)
        bookmarkViewModel = ViewModelProviders.of(this, factory).get(BookmarkViewModel::class.java)


        listAdapter =
            BookmarkListAdapter { bookmarkDisplayEntity: BookmarkDisplayEntity ->
                bookClicked(
                    bookmarkDisplayEntity
                )
            }

        recyclerBooks.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerBooks.adapter = listAdapter
        bookmarkViewModel.getAllBookmarks(Constants.SORT_VERSE_ASC, folderID)


        listAdapter.setOnItemClickListener(object : BookmarkListAdapter.ItemClickListener {
            override fun onDelete(bookmarkDisplayEntity: BookmarkDisplayEntity, position: Int) {
                getBookmarkUseCase.deleteBookmark(bookmarkDisplayEntity.bookmark_id)
                listAdapter.booksList.removeAt(position)
                listAdapter.notifyItemRemoved(position)

            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.bookmark_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return (when (item.itemId) {
            R.id.action_sort -> {
                sortAlert()
                true
            }
            R.id.action_filter -> {
                GlobalScope.launch(Dispatchers.Main) {
                    var bookmarkFolders: List<BookmarkFolderEntity> = async(Dispatchers.IO) {
                        return@async AppDatabase(context).folderDao().getAllBookmarkFolder()
                    }.await()

                    if (bookmarkFolders.isNotEmpty()) {
                        showBookmarkFoldersList(bookmarkFolders)
                    }
                }
                true
            }
            else ->
                false
        })
    }

    private fun sortAlert() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.label_sort))
        val listItems = arrayOf("Verse Ascending", "Verse Descending", "Date Ascending", "Date Descending")
        var selected = 0
        builder.setSingleChoiceItems(
            listItems, -1
        ) { _, i ->
            selected = i
        }

        builder.setPositiveButton(getString(R.string.ok)) { dialog, which ->
            when (selected) {
                0 -> {
                    bookmarkViewModel.getAllBookmarks(Constants.SORT_VERSE_ASC, folderID)
                }
                1 -> {
                    bookmarkViewModel.getAllBookmarks(Constants.SORT_VERSE_DESC, folderID)
                }
                2 -> {
                    bookmarkViewModel.getAllBookmarks(Constants.SORT_DATE_ASC, folderID)
                }
                3 -> {
                    bookmarkViewModel.getAllBookmarks(Constants.SORT_DATE_DESC, folderID)
                }
            }

            bookmarkViewModel.fetchLiveBookmarks().observe(this, Observer {
                listAdapter.updateList(it ?: emptyList())
            })
            dialog.dismiss()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showBookmarkFoldersList(listItems: List<BookmarkFolderEntity>) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.label_filter_folders))


        val list = ArrayList<String>()

        listItems.forEach {
            list.add(it.foldername)
        }

        val galleryImageUrlList = arrayOfNulls<String>(list.size)
        list.toArray(galleryImageUrlList)

        var selected = 0
        builder.setSingleChoiceItems(
            galleryImageUrlList, -1
        ) { dialogInterface, i ->
            selected = i
        }



        builder.setPositiveButton(getString(R.string.filter)) { dialog, which ->

            val filteredList = bookmarkViewModel.fetchLiveBookmarks().value!!.filter {
                it.folderName.equals(listItems.get(selected).foldername)
            }
            listAdapter.updateList(filteredList)
            dialog.dismiss()

        }

        builder.setNegativeButton(getString(R.string.cancel)) { dialog, which ->
            bookmarkViewModel.getAllBookmarks(Constants.SORT_VERSE_ASC, folderID)
            bookmarkViewModel.fetchLiveBookmarks().observe(this, Observer {
                listAdapter.updateList(it ?: emptyList())
            })
            dialog.dismiss()

        }


        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


    private fun bookClicked(bookmarkDisplayEntity: BookmarkDisplayEntity) {
        val bookDisplayEntity = BookDisplayEntity(
            bookmarkDisplayEntity.book_no,
            0,
            "MAL",
            bookmarkDisplayEntity.book,
            bookmarkDisplayEntity.book,
            bookmarkDisplayEntity.book
        )
        val action = ChaptersFragmentDirections.actionChapterToPager(bookDisplayEntity)
        action.setSelectedChapter(bookmarkDisplayEntity.actual_chapter_no)
        action.setSelectedVerse(bookmarkDisplayEntity.verse_no)
        activity?.let { Navigation.findNavController(it, R.id.nav_host_fragment).navigate(action) }
    }

    override fun onStart() {
        super.onStart()
        bookmarkViewModel.fetchLiveBookmarks().observe(this, Observer {
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
