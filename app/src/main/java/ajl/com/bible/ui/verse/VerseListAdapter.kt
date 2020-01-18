package ajl.com.bible.ui.verse

import ajl.com.bible.R
import ajl.com.bible.utility.Constants
import ajl.com.domain.entities.VerseDisplayEntity
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.book_item.view.heading
import kotlinx.android.synthetic.main.chapter_item.view.verse
import kotlinx.android.synthetic.main.versus_item.view.*


class VerseListAdapter(val clickListener: (VerseDisplayEntity) -> Unit, var fontSize: Int) :
    RecyclerView.Adapter<VerseListAdapter.VerseViewHolder>() {

    var mLongClickListener: LongClickListener? = null
    var verseList = mutableListOf<VerseDisplayEntity>()


    fun setOnItemClickListener(longClickListener: LongClickListener) {
        mLongClickListener = longClickListener
    }

    interface LongClickListener {
        fun onLongClick(verseDisplayEntity: VerseDisplayEntity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.versus_item, parent, false)
        return VerseViewHolder(view)
    }

    override fun getItemCount(): Int {
        return verseList.size
    }

    override fun onBindViewHolder(holder: VerseViewHolder, position: Int) {
        holder.bind(verseList[position], clickListener, mLongClickListener, fontSize)
    }


    class VerseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(
            verseDisplayEntity: VerseDisplayEntity, clickListener: (VerseDisplayEntity) -> Unit,
            longClickListener: LongClickListener?, fontSize: Int
        ) {
            with(itemView) {
                heading.text = verseDisplayEntity.heading
                val verseno = verseDisplayEntity.verse_no.toString()
                val verseText = verseDisplayEntity.verse
                val verseTextCombined = "$verseno  $verseText"
                val spannableString = SpannableString(verseTextCombined)

                verseParent.setBackgroundColor(Color.WHITE)
                if (verseDisplayEntity.selected) verseParent.setBackgroundColor(Color.GRAY)

                heading.visibility = View.GONE
                if (verseDisplayEntity.heading.isNotEmpty()) heading.visibility = View.VISIBLE



                ivTag.visibility = View.GONE
                if (verseDisplayEntity.bookMarked) {
                    verseParent.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.LightGrey
                        )
                    )
                    ivTag.visibility = View.VISIBLE
                }




                if (verseDisplayEntity.noted) {
                    val d = ContextCompat.getDrawable(verse.context, R.drawable.ic_note_blue)
                    d!!.setBounds(0, 0, d.intrinsicWidth, d.intrinsicHeight)
                    val span = ImageSpan(d, ImageSpan.ALIGN_BASELINE)
                    spannableString.setSpan(span, 1, 2, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                    verseParent.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            ajl.com.bible.R.color.LightGrey
                        )
                    )
                }



                if (verseDisplayEntity.hasFootnote) {
                    val d = ContextCompat.getDrawable(
                        verse.context,
                        ajl.com.bible.R.drawable.ic_verse_footnote
                    )
                    d!!.setBounds(0, 0, d.intrinsicWidth, d.intrinsicHeight)
                    val span = ImageSpan(d, ImageSpan.ALIGN_BASELINE)
                    spannableString.setSpan(
                        span,
                        verseTextCombined.length - 1,
                        verseTextCombined.length,
                        Spannable.SPAN_INCLUSIVE_EXCLUSIVE
                    )
                }


                verse.text = spannableString

                heading.textSize = fontSize.toFloat()
                verse.textSize = fontSize.toFloat()

                when (verseDisplayEntity.colorTag) {
                    Constants.TAG_GREEN -> verseParent.setBackgroundColor(Color.GREEN)
                    Constants.TAG_RED -> verseParent.setBackgroundColor(Color.parseColor("#971419"))
                    Constants.TAG_BLUE -> verseParent.setBackgroundColor(Color.parseColor("#ADD8E6"))
                    Constants.TAG_YELLOW -> verseParent.setBackgroundColor(Color.YELLOW)
                    // else ->   verseParent.setBackgroundColor(Color.WHITE)
                }

            }



            itemView.setOnClickListener {
                /*if (verseDisplayEntity.heading.length== 0)*/ clickListener(
                verseDisplayEntity
            )
            }

            itemView.setOnLongClickListener {
                verseDisplayEntity.selected = true
                /*  if (verseDisplayEntity.heading.length == 0)*/longClickListener?.onLongClick(
                verseDisplayEntity
            )
                return@setOnLongClickListener true
            }

        }
    }

    fun setTextFontSize(fontSize: Int) {
        this.fontSize = fontSize
        notifyDataSetChanged()
    }

    fun getVerseDisplayList(): MutableList<VerseDisplayEntity> {
        return verseList
    }

    fun updateList(list: List<VerseDisplayEntity>) {
        if (list.isNotEmpty()) {
            verseList.clear()
            verseList.addAll(list)
            notifyDataSetChanged()
        }
    }
}