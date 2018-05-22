package app.android.snappay.model.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CurrencyBean(
        val countryName: String,
        val currencyCode: String,
        val currencySymbol: String,
        val flagResId: Int
) : Parcelable
