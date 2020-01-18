package ajl.com.bible.ui.book

import ajl.com.bible.R
import ajl.com.bible.controllers.OnFragmentInteractionListener
import ajl.com.bible.utility.BookViewModelFactory
import ajl.com.data.db.AppDatabase
import ajl.com.data.repository.BookRepositoryImpl
import ajl.com.domain.entities.BookDisplayEntity
import ajl.com.domain.usecases.GetBooksUseCase
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
import kotlinx.android.synthetic.main.fragment_books.*

class BooksFragment : Fragment() {

    private lateinit var booksViewModel: BooksViewModel
    private lateinit var oldBookAdapter: BooksListAdapter
    private lateinit var newBookAdapter: BooksListAdapter
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_books, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.setTitle(getString(R.string.label_books))

        val a = GetBooksUseCase(BookRepositoryImpl(AppDatabase(context)))

        val factory = BookViewModelFactory(a)
        booksViewModel = ViewModelProviders.of(this, factory).get(BooksViewModel::class.java)


        oldBookAdapter = BooksListAdapter({ bookDisplayEntity: BookDisplayEntity -> bookClicked(bookDisplayEntity) })
        newBookAdapter = BooksListAdapter({ bookDisplayEntity: BookDisplayEntity -> bookClicked(bookDisplayEntity) })

        recyclerOldBooks.layoutManager = GridLayoutManager(context, 6, RecyclerView.VERTICAL, false)
        recyclerOldBooks.adapter = oldBookAdapter

        recyclerNewBooks.layoutManager = GridLayoutManager(context, 6, RecyclerView.VERTICAL, false)
        recyclerNewBooks.adapter = newBookAdapter



        booksViewModel.getBooks()
    }


    private fun bookClicked(bookDisplayEntity: BookDisplayEntity) {

        val action = BooksFragmentDirections.actionBookToChapters(bookDisplayEntity)
        action.setBook(bookDisplayEntity)
        activity?.let { Navigation.findNavController(it, R.id.nav_host_fragment).navigate(action) }

        // listener?.onFragmentInteraction(Constants.REDIRECT_CHAPTERS, bookDisplayEntity)
    }

    override fun onStart() {
        super.onStart()
        booksViewModel.fetchLiveBooks().observe(this, Observer {

            progressLayout.visibility = View.GONE
            oldBookAdapter.updateList(it.subList(0, 46))

            newBookAdapter.updateList(it.subList(46, 73))
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
