package android.websocket.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


/**
 * Created by Murat Yüksektepe on 24.11.2022.
 * muratyuksektepe.com
 * yuksektepemurat@gmail.com
 */
sealed class CurrencyRecyclerViewItems {

    @Parcelize
    class Title(
        @SerializedName("from")
        val title: String
    ) : CurrencyRecyclerViewItems(), Parcelable

    @Parcelize
    class Row(
        @SerializedName("name")
        val currencyName: String,
        @SerializedName("amount")
        val currencyAmount: Double
    ) : CurrencyRecyclerViewItems(), Parcelable
}