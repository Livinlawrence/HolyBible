package ajl.com.bible.ui.bookmark

import ajl.com.bible.R
import ajl.com.domain.entities.BookmarkFolderDisplayEntity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_folder.view.*
import kotlinx.android.synthetic.main.layout_note.view.folderName


class BookmarkFolderListAdapter(val clickListener: (BookmarkFolderDisplayEntity) -> Unit) :
    RecyclerView.Adapter<BookmarkFolderListAdapter.NewsViewHolder>() {

    var bookmarkFolderList = mutableListOf<BookmarkFolderDisplayEntity>()
    var deleteItemClickListener1: DeleteItemClickListener? = null

    fun setOnDeleteItemClickListener(deleteItemClickListener: DeleteItemClickListener) {
        deleteItemClickListener1 = deleteItemClickListener
    }

    interface DeleteItemClickListener {
        fun onDelete(BookmarkFolderDisplayEntity: BookmarkFolderDisplayEntity, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_folder, parent, false)
        return NewsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return bookmarkFolderList.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(bookmarkFolderList[position], clickListener, deleteItemClickListener1)
    }

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(
            BookmarkFolderDisplayEntity: BookmarkFolderDisplayEntity,
            clickListener: (BookmarkFolderDisplayEntity) -> Unit,
            deleteItemClickListener: DeleteItemClickListener?
        ) {


            with(itemView) {
                folderName.text =
                    BookmarkFolderDisplayEntity.folderName
            }


            itemView.setOnClickListener { clickListener(BookmarkFolderDisplayEntity) }

            itemView.ivDeleteBookMark.setOnClickListener {
                deleteItemClickListener?.onDelete(
                    BookmarkFolderDisplayEntity,
                    adapterPosition
                )
            }
        }
    }

    fun updateList(list: List<BookmarkFolderDisplayEntity>) {
        if (list.isNotEmpty()) {
            bookmarkFolderList.clear()
            bookmarkFolderList.addAll(list)
            notifyDataSetChanged()
        }
    }
}