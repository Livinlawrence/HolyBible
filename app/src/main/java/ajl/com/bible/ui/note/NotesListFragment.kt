package ajl.com.bible.ui.note

import ajl.com.bible.R
import ajl.com.bible.controllers.OnFragmentInteractionListener
import ajl.com.bible.ui.chapter.ChaptersFragmentDirections
import ajl.com.bible.utility.NoteViewModelFactory
import ajl.com.data.db.AppDatabase
import ajl.com.data.repository.NoteRepositoryImpl
import ajl.com.domain.entities.BookDisplayEntity
import ajl.com.domain.entities.NoteDisplayEntity
import ajl.com.domain.entities.NoteFolderDisplayEntity
import ajl.com.domain.usecases.GetNoteUseCase
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_bookmarks.*
import kotlinx.android.synthetic.main.layout_save_note.view.*

class NotesListFragment : Fragment() {

    private lateinit var noteViewModel: NoteViewModel
    private lateinit var noteListAdapter: NoteListAdapter
    private var listener: OnFragmentInteractionListener? = null
    var noteFolderId = 0

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


        val notesFolderEntity: NoteFolderDisplayEntity =
            NotesListFragmentArgs.fromBundle(arguments).noteFolderName
        noteFolderId = notesFolderEntity._id
        activity?.setTitle(getString(R.string.label_notes) + " of " + notesFolderEntity.folderName)

        val getNoteUseCase = GetNoteUseCase(NoteRepositoryImpl(AppDatabase(context)))

        val factory = NoteViewModelFactory(getNoteUseCase)
        noteViewModel = ViewModelProviders.of(this, factory).get(NoteViewModel::class.java)


        noteListAdapter =
            NoteListAdapter { noteDisplayEntity: NoteDisplayEntity -> noteClicked(noteDisplayEntity) }

        recyclerBooks.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerBooks.adapter = noteListAdapter
        noteViewModel.getAllNotes(noteFolderId)


        noteListAdapter.setOnItemClickListener(object : NoteListAdapter.ItemClickListener {
            override fun onDelete(noteDisplayEntity: NoteDisplayEntity, position: Int) {
                getNoteUseCase.deleteBookmark(noteDisplayEntity.note_id)
                noteListAdapter.booksList.removeAt(position)
                noteListAdapter.notifyItemRemoved(position)
            }

            override fun onEdit(noteDisplayEntity: NoteDisplayEntity, position: Int) {
                showAddNoteDialog(noteDisplayEntity, position)
            }
        })
    }


    private fun noteClicked(noteDisplayEntity: NoteDisplayEntity) {
        val bookDisplayEntity = BookDisplayEntity(
            noteDisplayEntity.book_no,
            0,
            "MAL",
            noteDisplayEntity.book,
            noteDisplayEntity.book,
            noteDisplayEntity.book
        )
        val action = ChaptersFragmentDirections.actionChapterToPager(bookDisplayEntity)
        action.setSelectedChapter(noteDisplayEntity.actual_chapter_no)
        action.setSelectedVerse(noteDisplayEntity.verse_no)
        activity?.let { Navigation.findNavController(it, R.id.nav_host_fragment).navigate(action) }
    }


    /*override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
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
    }*/


    private fun showAddNoteDialog(noteDisplayEntity: NoteDisplayEntity, position: Int) {
        val inflater = requireActivity().layoutInflater
        val itemView = inflater.inflate(R.layout.layout_save_note, null)
        val alertDialog: AlertDialog? = activity?.let {
            val builder = AlertDialog.Builder(it)

            itemView.tvHeading.text =
                noteDisplayEntity.book + "(" + noteDisplayEntity.chapter + ":" + noteDisplayEntity.verse_no + ")"
            itemView.tvVerse.text = noteDisplayEntity.verse
            itemView.etNote.setText(noteDisplayEntity.note)


            builder.apply {
                setTitle(getString(R.string.label_add_note) + " - " + noteDisplayEntity.foldername)
                setPositiveButton(
                    R.string.label_save
                ) { dialog, id ->

                    val userNote: String = itemView.etNote.text.toString().trim()
                    if (userNote.isNotEmpty()) {
                        dialog.dismiss()
                        val getNoteUseCase = GetNoteUseCase(NoteRepositoryImpl(AppDatabase(context)))
                        getNoteUseCase.updateNote(noteDisplayEntity.note_id, userNote)
                        noteDisplayEntity.note = userNote
                        noteListAdapter.notifyItemChanged(position)
                    }
                }
                setNegativeButton(R.string.cancel)
                { dialog, id ->
                    dialog.dismiss()
                }
                setView(itemView)
            }


            // Create the AlertDialog
            builder.create()
        }

        alertDialog?.show()

    }


    override fun onStart() {
        super.onStart()
        noteViewModel.notesList.observe(this, Observer {
            noteListAdapter.updateList(it ?: emptyList())
        })
    }

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
