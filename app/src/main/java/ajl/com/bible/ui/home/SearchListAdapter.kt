package ajl.com.bible.ui.home

import ajl.com.bible.R
import ajl.com.domain.entities.VerseDisplayEntity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.bookmark_item.view.*


class SearchListAdapter(private val clickListener: (VerseDisplayEntity) -> Unit) :
    RecyclerView.Adapter<SearchListAdapter.NewsViewHolder>() {

    var mutableList = mutableListOf<VerseDisplayEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_search_item, parent, false)
        return NewsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mutableList.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(mutableList[position], clickListener)
    }

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(verseDisplayEntity: VerseDisplayEntity, clickListener: (VerseDisplayEntity) -> Unit) {


            with(itemView) {
                author.text =
                    verseDisplayEntity.book + " " + verseDisplayEntity.chapter + ":" + verseDisplayEntity.verse_no
                verse.text = verseDisplayEntity.verse


            }


            itemView.setOnClickListener { clickListener(verseDisplayEntity) }
        }
    }

    fun updateList(list: List<VerseDisplayEntity>) {
        if (list.isNotEmpty()) {
            mutableList.clear()
            mutableList.addAll(list)
            notifyDataSetChanged()
        }
    }
}