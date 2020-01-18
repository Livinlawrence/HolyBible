package ajl.com.bible.ui.chapter

import ajl.com.bible.R
import ajl.com.domain.entities.ChapterDisplayEntity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.book_item.view.heading
import kotlinx.android.synthetic.main.chapter_item.view.*


class ChapterListAdapter(val clickListener: (Int) -> Unit) :
    RecyclerView.Adapter<ChapterListAdapter.NewsViewHolder>() {

    var booksList = mutableListOf<ChapterDisplayEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chapter_item, parent, false)
        return NewsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return booksList.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(booksList[position], clickListener)
    }

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(chapterDisplayEntity: ChapterDisplayEntity, clickListener: (Int) -> Unit) {
            with(itemView) {
                heading.text = chapterDisplayEntity.chapterNo.toString()


                val a = resources.getText(R.string.label_vakyangal)
                val b = chapterDisplayEntity.verseCount
                verse.text = "$a"
                verseCount.text = "(1 - $b)"
            }
            itemView.setOnClickListener { clickListener(adapterPosition) }
        }
    }

    fun updateList(list: List<ChapterDisplayEntity>) {
        if (list.isNotEmpty()) {
            booksList.clear()
            booksList.addAll(list)
            notifyDataSetChanged()
        }
    }
}