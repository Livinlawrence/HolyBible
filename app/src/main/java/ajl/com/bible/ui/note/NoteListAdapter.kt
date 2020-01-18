package ajl.com.bible.ui.note

import ajl.com.bible.R
import ajl.com.domain.entities.NoteDisplayEntity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.bookmark_item.view.author
import kotlinx.android.synthetic.main.bookmark_item.view.ivDelete
import kotlinx.android.synthetic.main.bookmark_item.view.verse
import kotlinx.android.synthetic.main.layout_note.view.*


class NoteListAdapter(val clickListener: (NoteDisplayEntity) -> Unit) :
    RecyclerView.Adapter<NoteListAdapter.NewsViewHolder>() {

    var booksList = mutableListOf<NoteDisplayEntity>()
    var itemClickListener1: ItemClickListener? = null

    fun setOnItemClickListener(itemClickListener: ItemClickListener) {
        itemClickListener1 = itemClickListener
    }

    interface ItemClickListener {
        fun onDelete(noteDisplayEntity: NoteDisplayEntity, position: Int)
        fun onEdit(noteDisplayEntity: NoteDisplayEntity, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_note, parent, false)
        return NewsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return booksList.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(booksList[position], clickListener, itemClickListener1)
    }

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(
            noteDisplayEntity: NoteDisplayEntity,
            clickListener: (NoteDisplayEntity) -> Unit,
            itemClickListener: ItemClickListener?
        ) {


            with(itemView) {
                author.text =
                    noteDisplayEntity.book + " " + noteDisplayEntity.chapter + ":" + noteDisplayEntity.verse_no
                verse.text = noteDisplayEntity.verse
                note.text = noteDisplayEntity.note
                folderName.text = noteDisplayEntity.foldername


            }


            itemView.setOnClickListener { clickListener(noteDisplayEntity) }

            itemView.ivDelete.setOnClickListener({ itemClickListener?.onDelete(noteDisplayEntity, adapterPosition) })
            itemView.ivEdit.setOnClickListener({ itemClickListener?.onEdit(noteDisplayEntity, adapterPosition) })
        }
    }

    fun updateList(list: List<NoteDisplayEntity>) {
        if (list.isNotEmpty()) {
            booksList.clear()
            booksList.addAll(list)
            notifyDataSetChanged()
        }
    }
}