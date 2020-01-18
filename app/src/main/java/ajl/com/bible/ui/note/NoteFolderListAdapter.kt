package ajl.com.bible.ui.note

import ajl.com.bible.R
import ajl.com.domain.entities.NoteFolderDisplayEntity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_folder.view.*
import kotlinx.android.synthetic.main.layout_note.view.folderName


class NoteFolderListAdapter(val clickListener: (NoteFolderDisplayEntity) -> Unit) :
    RecyclerView.Adapter<NoteFolderListAdapter.NewsViewHolder>() {

    var notesFoldersList = mutableListOf<NoteFolderDisplayEntity>()
    var itemClickListener1: ItemClickListener? = null

    fun setOnItemClickListener(itemClickListener: ItemClickListener) {
        itemClickListener1 = itemClickListener
    }

    interface ItemClickListener {
        fun onDelete(NoteFolderDisplayEntity: NoteFolderDisplayEntity, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_folder, parent, false)
        return NewsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return notesFoldersList.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(notesFoldersList[position], clickListener, itemClickListener1)
    }

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(
            NoteFolderDisplayEntity: NoteFolderDisplayEntity,
            clickListener: (NoteFolderDisplayEntity) -> Unit,
            itemClickListener: ItemClickListener?
        ) {


            with(itemView) {
                folderName.text =
                    NoteFolderDisplayEntity.folderName
            }


            itemView.setOnClickListener { clickListener(NoteFolderDisplayEntity) }

            itemView.ivDeleteBookMark.setOnClickListener {
                itemClickListener?.onDelete(
                    NoteFolderDisplayEntity,
                    adapterPosition
                )
            }
        }
    }

    fun updateList(list: List<NoteFolderDisplayEntity>) {
        if (list.isNotEmpty()) {
            notesFoldersList.clear()
            notesFoldersList.addAll(list)
            notifyDataSetChanged()
        }
    }
}