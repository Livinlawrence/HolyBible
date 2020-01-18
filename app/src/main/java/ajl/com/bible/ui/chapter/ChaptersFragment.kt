package ajl.com.bible.ui.chapter

import ajl.com.bible.R
import ajl.com.bible.controllers.OnFragmentInteractionListener
import ajl.com.bible.utility.ChapterViewModelFactory
import ajl.com.data.db.AppDatabase
import ajl.com.data.repository.ChapterRepositoryImpl
import ajl.com.domain.entities.BookDisplayEntity
import ajl.com.domain.usecases.GetChapterUseCase
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_chapters.*


class ChaptersFragment : Fragment() {

    private lateinit var chaptersViewModel: ChaptersViewModel
    private lateinit var listAdapter: ChapterListAdapter
    private var listener: OnFragmentInteractionListener? = null
    var book: BookDisplayEntity? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chapters, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.title = getString(R.string.label_chapters)
        book = ChaptersFragmentArgs.fromBundle(arguments).book


        val chapterUseCase = GetChapterUseCase(ChapterRepositoryImpl(AppDatabase(context)))

        val factory = ChapterViewModelFactory(chapterUseCase)
        chaptersViewModel = ViewModelProviders.of(this, factory).get(ChaptersViewModel::class.java)


        listAdapter =
            ChapterListAdapter { selectedChapterPosition: Int -> bookClicked(selectedChapterPosition) }

        recyclerChapters.layoutManager = GridLayoutManager(context, 5, RecyclerView.VERTICAL, false)
        recyclerChapters.adapter = listAdapter
        chaptersViewModel.fetchChapters(book!!)


        title_writer.text = book!!.bookMalName
    }

    private fun bookClicked(selectedPosition: Int) {
        val action = ChaptersFragmentDirections.actionChapterToPager(book!!)
        action.setSelectedChapter(selectedPosition + 1)
        activity?.let { Navigation.findNavController(it, R.id.nav_host_fragment).navigate(action) }
    }


    override fun onStart() {
        super.onStart()
        chaptersViewModel.fetchLiveChapters().observe(this, Observer {
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
