package ajl.com.bible.ui.chapter

import ajl.com.bible.R
import ajl.com.bible.controllers.OnFragmentInteractionListener
import ajl.com.bible.ui.verse.VerseFragment
import ajl.com.bible.utility.ChapterViewModelFactory
import ajl.com.bible.utility.Constants
import ajl.com.data.db.AppDatabase
import ajl.com.data.repository.ChapterRepositoryImpl
import ajl.com.domain.entities.BookDisplayEntity
import ajl.com.domain.entities.ChapterDisplayEntity
import ajl.com.domain.usecases.GetChapterUseCase
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.fragment_pager_chapters.*
import kotlinx.android.synthetic.main.layout_font_size.view.*


class ChaptersPagerFragment : Fragment(), VerseFragment.OnRecyclerScroller {


    private lateinit var chaptersViewModel: ChaptersViewModel
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var sharedPref: SharedPreferences
    private lateinit var mSectionsPagerAdapter: SectionsPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pager_chapters, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        sharedPref =
            context!!.getSharedPreferences(Constants.HOLY_BIBLE_PREFERENCE, Context.MODE_PRIVATE)
        activity?.title = getString(R.string.label_chapters)
        val book: BookDisplayEntity = ChaptersPagerFragmentArgs.fromBundle(arguments).book
        val selected = ChaptersPagerFragmentArgs.fromBundle(arguments).selectedChapter
        val verseSelected = ChaptersPagerFragmentArgs.fromBundle(arguments).selectedVerse

        val chapterUseCase = GetChapterUseCase(ChapterRepositoryImpl(AppDatabase(context)))
        val factory = ChapterViewModelFactory(chapterUseCase)
        chaptersViewModel = ViewModelProviders.of(this, factory).get(ChaptersViewModel::class.java)
        chaptersViewModel.fetchChapters(book)
        chaptersViewModel.fetchLiveChapters().observe(this, Observer {
            for (chapterDisplayEntity in it) {
                if (chapterDisplayEntity.chapterNo == selected) {
                    chapterDisplayEntity.selectedVerse = verseSelected
                    break
                }
            }
            setPager(it, selected - 1)
        })



        fab.setOnClickListener {
            showFontSizeDialog()
        }


        if (sharedPref.getBoolean(Constants.PREFERENCE_FIRST_TIME, true)) {
            layoutIntro.visibility = View.VISIBLE
        }



        layoutIntro.setOnClickListener {
            layoutIntro.visibility = View.GONE
            sharedPref.edit {
                putBoolean(
                    Constants.PREFERENCE_FIRST_TIME,
                    false
                )
            }
        }
    }


    override fun hideFab() {
        fab.visibility = View.GONE
    }

    override fun showFab() {
        fab.visibility = View.VISIBLE
    }

    private fun setPager(chapters: List<ChapterDisplayEntity>, selected: Int) {
        mSectionsPagerAdapter = SectionsPagerAdapter(
            childFragmentManager,
            chapters,
            sharedPref.getInt(Constants.PREFERENCE_FONT_SIZE, Constants.DEFAULT_READ_FONT_SIZE),
            this
        )
        chaptersPager.adapter = mSectionsPagerAdapter
        chaptersPager.currentItem = selected
        chaptersPager.offscreenPageLimit = 2


        try {
            val chapter: ChapterDisplayEntity = chapters[selected]
            var chapterNo = chapter.chapterNo
            /*if (chapter.chapterNo > 1) {
                chapterNo = chapter.chapterNo - 1
            }*/
            activity?.title =
                chapter.bookMalName + " " + (chapterNo) + ":" + "1-" + chapter.verseCount
        } catch (e: Exception) {
            e.printStackTrace()
        }

        chaptersPager.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageSelected(position: Int) {

                val chapterSelected: ChapterDisplayEntity = chapters[position]
                activity?.title =
                    chapterSelected.bookMalName + " " + chapterSelected.chapterNo + ":" + "1-" + chapterSelected.verseCount


            }

            override fun onPageScrolled(pos: Int, arg1: Float, arg2: Int) {

            }

            override fun onPageScrollStateChanged(pos: Int) {

            }
        })

    }

    private fun showFontSizeDialog() {
        val inflater = requireActivity().layoutInflater

        val itemView = inflater.inflate(R.layout.layout_font_size, null)

        val alertDialog: AlertDialog? = activity?.let {
            val builder = AlertDialog.Builder(it)


            builder.apply {

                setTitle(getString(R.string.label_set_font_size))
                setPositiveButton(
                    R.string.ok
                ) { dialog, id ->
                    dialog.dismiss()
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

        itemView.seekBar.progress =
            sharedPref.getInt(Constants.PREFERENCE_FONT_SIZE, Constants.DEFAULT_READ_FONT_SIZE)
        itemView.tvProgress.text = "font size " + itemView.seekBar.progress + " sp"
        itemView.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                itemView.tvProgress.text = "font size $progress sp"
                mSectionsPagerAdapter.updateFontSize(progress)
                sharedPref.edit {
                    putInt(
                        Constants.PREFERENCE_FONT_SIZE,
                        progress
                    )
                }
            }

            override fun onStartTrackingTouch(arg0: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
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
