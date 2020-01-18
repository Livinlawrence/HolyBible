package ajl.com.bible.ui.searchhistory

import ajl.com.bible.R
import ajl.com.domain.entities.SearchHistoryDisplayEntity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_recent_search_item.view.*


class RecentSearchListAdapter(private val clickListener: (SearchHistoryDisplayEntity) -> Unit) :
    RecyclerView.Adapter<RecentSearchListAdapter.NewsViewHolder>() {

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
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_recent_search_item, parent, false)
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
                tvRecent.text =
                    searchHistoryDisplayEntity.searchKeyword
            }


            itemView.setOnClickListener { clickListener(searchHistoryDisplayEntity) }


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