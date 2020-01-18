package ajl.com.bible.ui.note

import ajl.com.bible.R
import ajl.com.bible.controllers.OnFragmentInteractionListener
import ajl.com.bible.extensions.getViewModel
import ajl.com.data.db.AppDatabase
import ajl.com.data.repository.NoteRepositoryImpl
import ajl.com.domain.entities.NoteFolderDisplayEntity
import ajl.com.domain.usecases.GetNoteUseCase
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

class NoteFoldersListFragment : Fragment() {


    private lateinit var noteListAdapter: NoteFolderListAdapter
    private var listener: OnFragmentInteractionListener? = null

    val getNotesUsecase = GetNoteUseCase(NoteRepositoryImpl(AppDatabase(context)))

    private val noteViewModel by lazy {
        getViewModel { NoteViewModel(getNotesUsecase) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setHasOptionsMenu(true)
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


        activity?.title = getString(R.string.label_note_folders)

        noteListAdapter =
            NoteFolderListAdapter { notesFolderEntity: NoteFolderDisplayEntity ->
                noteClicked(
                    notesFolderEntity
                )
            }

        recyclerBooks.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerBooks.adapter = noteListAdapter

        noteViewModel.getNoteDisplayFolders()
        noteViewModel.notesFolderDisplayList.observe(this, Observer {
            noteListAdapter.updateList(it ?: emptyList())
        })

        noteListAdapter.setOnItemClickListener(object : NoteFolderListAdapter.ItemClickListener {
            override fun onDelete(noteFolderDisplayEntity: NoteFolderDisplayEntity, position: Int) {
                getNotesUsecase.deleteNoteFolder(noteFolderDisplayEntity._id)
                noteListAdapter.notesFoldersList.removeAt(position)
                noteListAdapter.notifyItemRemoved(position)
            }
        })
    }


    private fun noteClicked(notesFolderEntity: NoteFolderDisplayEntity) {
        val action = NoteFoldersListFragmentDirections.actionNoteFolderToNotes(notesFolderEntity)
        activity?.let { Navigation.findNavController(it, R.id.nav_host_fragment).navigate(action) }
    }

/*
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.note_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return (when (item.itemId) {

            R.id.action_filter -> {
                GlobalScope.launch(Dispatchers.Main) {
                    val notesFolders: List<NotesFolderEntity> = async(Dispatchers.IO) {
                        return@async AppDatabase(context).folderDao().getAllNotesFolder()
                    }.await()

                    if (notesFolders.isNotEmpty()) {
                        showNotesFoldersList(notesFolders)
                    }
                }
                true
            }
            else ->
                false
        })
    }


    private fun showNotesFoldersList(listItems: List<NotesFolderEntity>) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.label_filter_note_folder))

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
            if (selected == -1) return@setPositiveButton
            dialog.dismiss()


            val filteredList = noteViewModel.fetchLiveNotes().value!!.filter {
                it.foldername.equals(listItems.get(selected).foldername)
            }
            noteListAdapter.updateList(filteredList)

        }
        builder.setNegativeButton(getString(R.string.cancel)) { dialog, which ->
            dialog.dismiss()
            noteViewModel.getAllNotes()
            noteViewModel.fetchLiveNotes().observe(this, Observer {
                noteListAdapter.updateList(it ?: emptyList())
            })
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    */


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}
