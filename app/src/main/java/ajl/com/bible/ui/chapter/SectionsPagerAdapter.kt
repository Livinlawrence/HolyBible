package ajl.com.bible.ui.chapter

import ajl.com.bible.ui.verse.VerseFragment
import ajl.com.domain.entities.ChapterDisplayEntity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter


class SectionsPagerAdapter(
    supportFragmentManager: FragmentManager,
    private val chapters: List<ChapterDisplayEntity>,
    var fontSize: Int,
    var onRecyclerScroller: VerseFragment.OnRecyclerScroller
) :
    FragmentStatePagerAdapter(supportFragmentManager) {

    override fun getItem(position: Int): Fragment {
        val fragment = VerseFragment.newInstance(chapters[position])
        fragment.setOnRecyclerScroller(onRecyclerScroller)
        return fragment
    }

    override fun getCount(): Int {
        return chapters.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return chapters[position].bookMalName
    }

    override fun getItemPosition(`object`: Any): Int {
        if (`object` is VerseFragment) {
            `object`.update(fontSize)
        }
        //don't return POSITION_NONE, avoid fragment recreation.
        return super.getItemPosition(`object`)
    }

    fun updateFontSize(fontSize: Int) {
        this.fontSize = fontSize
        notifyDataSetChanged()
    }
}