package ajl.com.bible.ui.footnote

import ajl.com.bible.R
import ajl.com.bible.controllers.OnFragmentInteractionListener
import ajl.com.bible.utility.FoteNoteViewModelFactory
import ajl.com.data.db.AppDatabase
import ajl.com.data.repository.FootNoteRepositoryImpl
import ajl.com.domain.entities.FootNoteDisplayEntity
import ajl.com.domain.usecases.GetFootNoteUseCase
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_bookmarks.*

class FootNoteFragment : Fragment() {

    private lateinit var footNoteViewModel: FootNoteViewModel
    private lateinit var listAdapter: FootNoteListAdapter
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_foot_note, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        activity?.setTitle(getString(R.string.label_fote_notes))
        val getFootNoteUseCase = GetFootNoteUseCase(FootNoteRepositoryImpl(AppDatabase(context)))

        val factory = FoteNoteViewModelFactory(getFootNoteUseCase)

        footNoteViewModel = ViewModelProviders.of(this, factory).get(FootNoteViewModel::class.java)


        listAdapter =
            FootNoteListAdapter({ footNote: FootNoteDisplayEntity -> bookClicked(footNote) })

        recyclerBooks.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerBooks.adapter = listAdapter
        footNoteViewModel.getFootNotes()


    }


    private fun bookClicked(footNote: FootNoteDisplayEntity) {

        /*  val chapter = ChapterDisplayEntity(
              bookmarkDisplayEntity._id,
              bookmarkDisplayEntity.book_no,
              bookmarkDisplayEntity.testament,
              bookmarkDisplayEntity.bookNameEng,
              bookmarkDisplayEntity.book,
              bookmarkDisplayEntity.chapter,
              bookmarkDisplayEntity.verse_no
          )
          val action = BookmarkFragmentDirections.actionChapterToVerse(chapter)


          action.setChapter(chapter)
          activity?.let { Navigation.findNavController(it, R.id.nav_host_fragment).navigate(action) }*/

    }

    override fun onStart() {
        super.onStart()
        footNoteViewModel.fetchLiveFoteNotes().observe(this, Observer {
            listAdapter.updateList(it ?: emptyList())
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
