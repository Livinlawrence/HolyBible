package ajl.com.bible.ui.bookmark

import ajl.com.bible.R
import ajl.com.bible.controllers.OnFragmentInteractionListener
import ajl.com.bible.extensions.getViewModel
import ajl.com.data.db.AppDatabase
import ajl.com.data.repository.BookmarkRepositoryImpl
import ajl.com.domain.entities.BookmarkFolderDisplayEntity
import ajl.com.domain.usecases.GetBookmarkUseCase
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_bookmarks.*


class BookmarkFoldersFragment : Fragment() {


    private lateinit var listAdapter: BookmarkFolderListAdapter
    private var listener: OnFragmentInteractionListener? = null

    val getBookmarkUseCase = GetBookmarkUseCase(BookmarkRepositoryImpl(AppDatabase(context)))

    private val bookmarkViewModel by lazy {
        getViewModel { BookmarkViewModel(getBookmarkUseCase) }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setHasOptionsMenu(true)
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


        activity?.title = getString(R.string.label_bookmark_folders)


        listAdapter =
            BookmarkFolderListAdapter { bookmarkFolderEntity: BookmarkFolderDisplayEntity ->
                folderSelected(
                    bookmarkFolderEntity
                )
            }

        listAdapter.setOnDeleteItemClickListener(object :
            BookmarkFolderListAdapter.DeleteItemClickListener {
            override fun onDelete(
                bookmarkFolderEntity: BookmarkFolderDisplayEntity,
                position: Int
            ) {
                getBookmarkUseCase.deleteBookmarkFolder(bookmarkFolderEntity._id)
                listAdapter.bookmarkFolderList.removeAt(position)
                listAdapter.notifyItemRemoved(position)
            }
        })

        recyclerBooks.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerBooks.adapter = listAdapter


        bookmarkViewModel.getAllBookmarkFolders()
        bookmarkViewModel.bookmarkFoldersList.observe(this, Observer {
            listAdapter.updateList(it ?: emptyList())
        })


        /*listAdapter.setOnItemClickListener(object : BookmarkListAdapter.DeleteItemClickListener {
            override fun onDelete(bookmarkDisplayEntity: BookmarkDisplayEntity, position: Int) {
                getBookmarkUseCase.deleteBookmark(bookmarkDisplayEntity.bookmark_id)
                listAdapter.notesFoldersList.removeAt(position)
                listAdapter.notifyItemRemoved(position)

            }
        })*/
    }

    /* override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
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
         builder.setSingleChoiceItems(listItems, -1,
             { dialogInterface, i ->
                 selected = i
             })

         builder.setPositiveButton(getString(R.string.ok)) { dialog, which ->
             when (selected) {
                 0 -> {
                     bookmarkViewModel.getAllBookmarks(Constants.SORT_VERSE_ASC)
                 }
                 1 -> {
                     bookmarkViewModel.getAllBookmarks(Constants.SORT_VERSE_DESC)
                 }
                 2 -> {
                     bookmarkViewModel.getAllBookmarks(Constants.SORT_DATE_ASC)
                 }
                 3 -> {
                     bookmarkViewModel.getAllBookmarks(Constants.SORT_DATE_DESC)
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
         builder.setSingleChoiceItems(galleryImageUrlList, -1,
             { dialogInterface, i ->
                 selected = i
             })



         builder.setPositiveButton(getString(R.string.filter)) { dialog, which ->

             val filteredList = bookmarkViewModel.fetchLiveBookmarks().value!!.filter {
                 it.folderName.equals(listItems.get(selected).foldername)
             }
             listAdapter.updateList(filteredList)
             dialog.dismiss()

         }

         builder.setNegativeButton(getString(R.string.cancel)) { dialog, which ->
             bookmarkViewModel.getAllBookmarks(Constants.SORT_VERSE_ASC)
             bookmarkViewModel.fetchLiveBookmarks().observe(this, Observer {
                 listAdapter.updateList(it ?: emptyList())
             })
             dialog.dismiss()

         }


         val dialog: AlertDialog = builder.create()
         dialog.show()
     }
 */

    private fun folderSelected(bookmarkFolderEntity: BookmarkFolderDisplayEntity) {
        val action = BookmarkFoldersFragmentDirections.actionBookmarkFolderToBookmarkItem(bookmarkFolderEntity)
        activity?.let { Navigation.findNavController(it, R.id.nav_host_fragment).navigate(action) }
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
