package ajl.com.bible.ui.bookmark

import ajl.com.bible.R
import ajl.com.domain.entities.BookmarkDisplayEntity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.bookmark_item.view.*


class BookmarkListAdapter(private val clickListener: (BookmarkDisplayEntity) -> Unit) :
    RecyclerView.Adapter<BookmarkListAdapter.NewsViewHolder>() {

    var booksList = mutableListOf<BookmarkDisplayEntity>()
    var itemClickListener1: ItemClickListener? = null


    fun setOnItemClickListener(itemClickListener: ItemClickListener) {
        itemClickListener1 = itemClickListener
    }

    interface ItemClickListener {
        fun onDelete(bookmarkDisplayEntity: BookmarkDisplayEntity, position: Int)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bookmark_item, parent, false)
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
            bookmarkDisplayEntity: BookmarkDisplayEntity,
            clickListener: (BookmarkDisplayEntity) -> Unit, itemClickListener: ItemClickListener?
        ) {


            with(itemView) {
                author.text =
                    bookmarkDisplayEntity.book + " " + bookmarkDisplayEntity.chapter + ":" + bookmarkDisplayEntity.verse_no
                verse.text = bookmarkDisplayEntity.verse
                folderName.text = bookmarkDisplayEntity.folderName

            }


            itemView.setOnClickListener { clickListener(bookmarkDisplayEntity) }

            itemView.ivDelete.setOnClickListener({
                itemClickListener?.onDelete(
                    bookmarkDisplayEntity,
                    adapterPosition
                )
            })
        }
    }

    fun updateList(list: List<BookmarkDisplayEntity>) {
        if (list.isNotEmpty()) {
            booksList.clear()
            booksList.addAll(list)
            notifyDataSetChanged()
        }
    }
}