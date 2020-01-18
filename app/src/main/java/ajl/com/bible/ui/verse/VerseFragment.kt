package ajl.com.bible.ui.verse

import ajl.com.bible.R
import ajl.com.bible.controllers.OnFragmentInteractionListener
import ajl.com.bible.utility.Constants
import ajl.com.bible.utility.VerseViewModelFactory
import ajl.com.data.db.AppDatabase
import ajl.com.data.entity.FootnoteVerseEntity
import ajl.com.data.repository.VerseRepositoryImpl
import ajl.com.domain.entities.ChapterDisplayEntity
import ajl.com.domain.entities.VerseDisplayEntity
import ajl.com.domain.usecases.GetVerseUseCase
import android.app.AlertDialog
import android.content.*
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_verse.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class VerseFragment : Fragment() {

    private lateinit var verseViewModel: VerseViewModel
    private lateinit var listAdapter: VerseListAdapter
    private var showClipBoardMenu: Boolean = false
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var sharedPref: SharedPreferences
    var selectedVerse = 0
    var chapter: ChapterDisplayEntity? = null
    private lateinit var onRecyclerScroller: OnRecyclerScroller

    interface OnRecyclerScroller {
        fun hideFab()
        fun showFab()
    }


    public fun setOnRecyclerScroller(onRecyclerScroller: OnRecyclerScroller) {
        this.onRecyclerScroller = onRecyclerScroller
    }
    companion object {
        fun newInstance(chapter: ChapterDisplayEntity): VerseFragment {
            val fragment = VerseFragment()
            fragment.chapter = chapter
            fragment.selectedVerse = chapter.selectedVerse
            return fragment
        }
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
        return inflater.inflate(R.layout.fragment_verse, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPref =
            context!!.getSharedPreferences(Constants.HOLY_BIBLE_PREFERENCE, Context.MODE_PRIVATE)


        if (chapter == null) {
            chapter = arguments?.let { VerseFragmentArgs.fromBundle(it).chapter }
            selectedVerse = arguments?.let { VerseFragmentArgs.fromBundle(it).selectedVerse }!!
        }

        val getVerseUseCase = GetVerseUseCase(VerseRepositoryImpl(AppDatabase(context)))

        val factory = VerseViewModelFactory(getVerseUseCase)
        verseViewModel = ViewModelProviders.of(this, factory).get(VerseViewModel::class.java)


        listAdapter = VerseListAdapter(
            { verse: VerseDisplayEntity -> verseClicked(verse) },
            sharedPref.getInt(Constants.PREFERENCE_FONT_SIZE, Constants.DEFAULT_READ_FONT_SIZE)
        )

        recyclerVersus.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerVersus.adapter = listAdapter


        verseViewModel.fetchVerse(chapter!!)

        //  title_wrote.text = chapter.bookMalName + " " + chapter.chapterNo + ":" + "1-" + chapter.verseCount


        listAdapter.setOnItemClickListener(object : VerseListAdapter.LongClickListener {
            override fun onLongClick(verseDisplayEntity: VerseDisplayEntity) {


                val bottomSheetFragment = BottomSheetFragment()
                bottomSheetFragment.setVerseDisplay(verseDisplayEntity)
                bottomSheetFragment.verseList = getAllSelectedVerse()
                bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
                bottomSheetFragment.setOnBookmarkListenr(object :
                    BottomSheetFragment.OnBottomSheetItemSelectedListener {
                    override fun onBookmarkAdded() {
                        verseViewModel.fetchVerse(chapter!!)
                        selectedVerse = verseDisplayEntity.verse_no
                        loadRoomVerses()
                    }

                    override fun onMultiVerseSelection() {
                        showClipBoardMenu = true
                        activity?.invalidateOptionsMenu()
                    }

                    override fun noteAdded() {
                        verseViewModel.fetchVerse(chapter!!)
                        selectedVerse = verseDisplayEntity.verse_no
                        loadRoomVerses()
                    }

                    override fun colorUpdated() {
                        verseViewModel.fetchVerse(chapter!!)
                        selectedVerse = verseDisplayEntity.verse_no
                        loadRoomVerses()
                    }
                })
            }
        })


        recyclerVersus.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    onRecyclerScroller.hideFab()
                } else if (dy < 0) {
                    onRecyclerScroller.showFab()
                }
            }
        })

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return (when (item.itemId) {
            R.id.action_copy -> {
                showClipBoardMenu = false
                activity?.invalidateOptionsMenu()
                copySelected()
                true
            }
            R.id.action_share -> {
                showClipBoardMenu = false
                activity?.invalidateOptionsMenu()
                shareSelected()
                true
            }
            else ->
                false
        })
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        if (menu!!.hasVisibleItems()) {

            if (menu.size() == 3) {
                menu.getItem(0)?.isVisible = !showClipBoardMenu
                menu.getItem(1)?.isVisible = showClipBoardMenu
                menu.getItem(2)?.isVisible = showClipBoardMenu
            } else if (menu.size() == 2) {
                menu.getItem(0)?.isVisible = showClipBoardMenu
                menu.getItem(1)?.isVisible = showClipBoardMenu
            }
        }
        //showClipBoardMenu = false
        super.onPrepareOptionsMenu(menu)
    }



    private fun verseClicked(verse: VerseDisplayEntity) {
        selectedItem.visibility = View.VISIBLE
        selectedItem.text = verse.book + " " + verse.chapter + " : " + verse.verse_no


        if (!showClipBoardMenu) {
            listAdapter.getVerseDisplayList().forEach { verseDisplayEntity: VerseDisplayEntity ->
                verseDisplayEntity.selected = false
            }
        }

        verse.selected = verse.selected != true

        listAdapter.notifyDataSetChanged()


        if (verse.hasFootnote) {
            GlobalScope.launch(Dispatchers.Main) {
                var footnote: FootnoteVerseEntity = async(Dispatchers.IO) {
                    return@async AppDatabase(context).footNoteDao().getFootnoteFromVerse(verse._id)
                }.await()
                showFootnoteAlert(footnote)
            }
        }
    }


    override fun onStart() {
        super.onStart()
        loadRoomVerses()
    }

    private fun showFootnoteAlert(footnote: FootnoteVerseEntity) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.label_foot_note))
        builder.setMessage(footnote.footnote)
        builder.setPositiveButton(getString(R.string.ok)) { dialog, which ->
            dialog.dismiss()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun loadRoomVerses() {
        verseViewModel.fetchLiveVerse().observe(this, Observer {
            searchProgressLayout.visibility = View.GONE
            var selected = 0
            for (i in it.indices) {
                if (it[i].verse_no == selectedVerse) {
                    it[i].selected = true
                    selected = i
                    break
                }
            }
            listAdapter.updateList(it ?: emptyList())
            recyclerVersus.scrollToPosition(selected)
        })
    }


    private fun getSelectedVerseText(): String {
        var shareText = ""

        var startVerseAdded = false
        var startVerse: VerseDisplayEntity? = null
        var endVerse: VerseDisplayEntity? = null
        listAdapter.getVerseDisplayList().forEach { verseDisplayEntity: VerseDisplayEntity ->
            if (verseDisplayEntity.selected) {
                shareText =
                    shareText + "" + verseDisplayEntity.verse + ":" + verseDisplayEntity.book + "(" + verseDisplayEntity.chapter + ":" + verseDisplayEntity.verse_no + ")"
                if (!startVerseAdded) {
                    startVerse = verseDisplayEntity
                    startVerseAdded = true
                }
                endVerse = verseDisplayEntity
            }
        }

        if (shareText.isEmpty()) {
            Toast.makeText(
                context,
                getString(R.string.message_pls_select_an_verse),
                Toast.LENGTH_SHORT
            ).show()
            return ""
        }
        // endVerse?.let { verseClicked(it) }
        return shareText

    }

    private fun getAllSelectedVerse(): MutableList<VerseDisplayEntity> {
        var verseList = mutableListOf<VerseDisplayEntity>()
        listAdapter.getVerseDisplayList().forEach { verseDisplayEntity: VerseDisplayEntity ->
            if (verseDisplayEntity.selected) {
                verseList.add(verseDisplayEntity)
            }
        }
        return verseList
    }


    private fun copySelected() {
        val text = getSelectedVerseText()
        if (text.isNotEmpty()) {
            val clipboard =
                context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
            val clip = ClipData.newPlainText("Content", text)
            clipboard!!.setPrimaryClip(clip)
            Toast.makeText(context, getString(R.string.message_verse_copied), Toast.LENGTH_SHORT)
                .show()

        }
    }

    private fun shareSelected() {
        val text = getSelectedVerseText()
        if (text.isNotEmpty()) {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, getSelectedVerseText())
                type = "text/plain"
            }
            startActivity(sendIntent)
        }
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

    fun update(fontSize: Int) {
        listAdapter.setTextFontSize(fontSize)
    }

}
