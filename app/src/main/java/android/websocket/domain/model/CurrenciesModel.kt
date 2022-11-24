package android.websocket.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue


/**
 * Created by Murat Yüksektepe on 24.11.2022.
 * muratyuksektepe.com
 * yuksektepemurat@gmail.com
 */
@Parcelize
class CurrenciesModel(

    @SerializedName("from")
    val title: String,

    @SerializedName("list")
    val row: @RawValue MutableList<CurrencyRecyclerViewItems.Row>

) : Parcelable