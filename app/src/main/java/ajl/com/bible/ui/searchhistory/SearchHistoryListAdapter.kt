package ajl.com.bible.ui.searchhistory

import ajl.com.bible.R
import ajl.com.bible.utility.Constants
import ajl.com.domain.entities.SearchHistoryDisplayEntity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.bookmark_item.view.ivDelete
import kotlinx.android.synthetic.main.layout_navigation.view.searchCount
import kotlinx.android.synthetic.main.search_history_item.view.*
import java.text.SimpleDateFormat
import java.util.*


class SearchHistoryListAdapter(private val clickListener: (SearchHistoryDisplayEntity) -> Unit) :
    RecyclerView.Adapter<SearchHistoryListAdapter.NewsViewHolder>() {

    var searchHistoryList = mutableListOf<SearchHistoryDisplayEntity>()
    var itemClickListener1: ItemClickListener? = null


    fun setOnItemClickListener(itemClickListener: ItemClickListener) {
        itemClickListener1 = itemClickListener
    }

    interface ItemClickListener {
        fun onDelete(searchHistoryDisplayEntity: SearchHistoryDisplayEntity, position: Int)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.search_history_item, parent, false)
        return NewsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return searchHistoryList.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(searchHistoryList[position], clickListener, itemClickListener1)
    }

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(
            searchHistoryDisplayEntity: SearchHistoryDisplayEntity,
            clickListener: (SearchHistoryDisplayEntity) -> Unit,
            itemClickListener: ItemClickListener?
        ) {
            with(itemView) {
                searchKeyword.text =
                    searchHistoryDisplayEntity.searchKeyword
                val formatter = SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault())
                searchDate.text =
                    "Searched on " + formatter.format(Date(searchHistoryDisplayEntity.date))
                searchCount.text =
                    searchHistoryDisplayEntity.resultCount.toString() + " Results found"
            }


            itemView.setOnClickListener { clickListener(searchHistoryDisplayEntity) }

            itemView.ivDelete.setOnClickListener {
                itemClickListener?.onDelete(
                    searchHistoryDisplayEntity,
                    adapterPosition
                )
            }
        }
    }

    fun updateList(list: List<SearchHistoryDisplayEntity>) {
        if (list.isNotEmpty()) {
            searchHistoryList.clear()
            searchHistoryList.addAll(list)
            notifyDataSetChanged()
        }
    }
}