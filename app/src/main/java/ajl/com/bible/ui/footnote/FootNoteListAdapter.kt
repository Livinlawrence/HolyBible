package ajl.com.bible.ui.footnote

import ajl.com.bible.R
import ajl.com.domain.entities.FootNoteDisplayEntity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.bookmark_item.view.*


class FootNoteListAdapter(val clickListener: (FootNoteDisplayEntity) -> Unit) :
    RecyclerView.Adapter<FootNoteListAdapter.FootNoteHolder>() {

    var foteNoteList = mutableListOf<FootNoteDisplayEntity>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FootNoteHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fote_note_item, parent, false)
        return FootNoteHolder(view)
    }

    override fun getItemCount(): Int {
        return foteNoteList.size
    }

    override fun onBindViewHolder(holder: FootNoteHolder, position: Int) {
        holder.bind(foteNoteList[position], clickListener)
    }

    class FootNoteHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(
            footNoteDisplayEntity: FootNoteDisplayEntity,
            clickListener: (FootNoteDisplayEntity) -> Unit
        ) {


            with(itemView) {
                author.text =
                    footNoteDisplayEntity.book + " " + footNoteDisplayEntity.chapter_no + ":" + footNoteDisplayEntity.verse_no
                verse.text = footNoteDisplayEntity.footnote
            }
            itemView.setOnClickListener { clickListener(footNoteDisplayEntity) }
        }
    }

    fun updateList(list: List<FootNoteDisplayEntity>) {
        if (list.isNotEmpty()) {
            foteNoteList.clear()
            foteNoteList.addAll(list)
            notifyDataSetChanged()
        }
    }
}