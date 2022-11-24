package android.websocket.presentation.adapter

import android.websocket.databinding.ListItemRowBinding
import android.websocket.databinding.ListItemTitleBinding
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding


/**
 * Created by Murat Yüksektepe on 24.11.2022.
 * muratyuksektepe.com
 * yuksektepemurat@gmail.com
 */
sealed class CurrencyItemsViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

    class TitleViewHolder(private val binding: ListItemTitleBinding) : CurrencyItemsViewHolder(binding) {
        fun bind(title: String) {
            binding.txtCurrencyTitle.text = title
        }
    }

    class RowViewHolder(private val binding: ListItemRowBinding) : CurrencyItemsViewHolder(binding) {
        fun bind(currencyName: String, currencyAmount: Double) {
            binding.txtCurrencyAmount.text = currencyAmount.toString()
            binding.txtCurrencyName.text = currencyName
        }
    }
}