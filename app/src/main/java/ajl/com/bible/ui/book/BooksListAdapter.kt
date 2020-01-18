package ajl.com.bible.ui.book

import ajl.com.bible.R
import ajl.com.domain.entities.BookDisplayEntity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.book_item.view.*


class BooksListAdapter(val clickListener: (BookDisplayEntity) -> Unit) :
    RecyclerView.Adapter<BooksListAdapter.NewsViewHolder>() {

    var booksList = mutableListOf<BookDisplayEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.book_item, parent, false)
        return NewsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return booksList.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(booksList[position], clickListener)
    }

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(bookDisplayEntity: BookDisplayEntity, clickListener: (BookDisplayEntity) -> Unit) {


            with(itemView) {
                heading.text = bookDisplayEntity.bookMalShortName


                /*val a = resources.getText(R.string.label_adyayam)
                val b = bookDisplayEntity.chapterCount.toString()
                chapterCount.text = "$a 1 - $b"

                testament.visibility = View.GONE
                if (bookDisplayEntity._id == 1) {
                    testament.visibility = View.VISIBLE
                    testament.text = resources.getText(R.string.label_old_testament)
                } else if (bookDisplayEntity._id == 47) {
                    testament.visibility = View.VISIBLE
                    testament.text = resources.getText(R.string.label_new_testament)
                }*/

            }


            itemView.setOnClickListener { clickListener(bookDisplayEntity) }
        }
    }

    fun updateList(list: List<BookDisplayEntity>) {
        if (list.isNotEmpty()) {
            booksList.clear()
            booksList.addAll(list)
            notifyDataSetChanged()
        }
    }
}