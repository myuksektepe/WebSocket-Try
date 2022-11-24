package android.websocket.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.websocket.R
import android.websocket.databinding.ListItemRowBinding
import android.websocket.databinding.ListItemTitleBinding
import android.websocket.domain.model.CurrencyRecyclerViewItems
import androidx.recyclerview.widget.RecyclerView


/**
 * Created by Murat Yüksektepe on 24.11.2022.
 * muratyuksektepe.com
 * yuksektepemurat@gmail.com
 */
class CurrencyAdapter : RecyclerView.Adapter<CurrencyItemsViewHolder>() {

    var items = listOf<CurrencyRecyclerViewItems>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is CurrencyRecyclerViewItems.Title -> R.layout.list_item_title
            is CurrencyRecyclerViewItems.Row -> R.layout.list_item_row
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyItemsViewHolder {
        return when (viewType) {
            R.layout.list_item_title -> CurrencyItemsViewHolder.TitleViewHolder(
                ListItemTitleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            R.layout.list_item_row -> CurrencyItemsViewHolder.RowViewHolder(
                ListItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            else -> throw  IllegalArgumentException("Invalid ViewType!!")
        }
    }

    override fun onBindViewHolder(holder: CurrencyItemsViewHolder, position: Int) {
        when (holder) {
            is CurrencyItemsViewHolder.TitleViewHolder -> holder.bind(
                (items[position] as CurrencyRecyclerViewItems.Title).title
            )
            is CurrencyItemsViewHolder.RowViewHolder -> holder.bind(
                (items[position] as CurrencyRecyclerViewItems.Row).currencyName,
                (items[position] as CurrencyRecyclerViewItems.Row).currencyAmount,
            )
        }
    }
}