package app.android.snappay.model.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WalletBean(
        val user_id: String,
        val amount: Double,
        val left_amount: Double,
        val added_on: String, // date format 19/apr/2018
        val currency_type: String, // currency iso code basically e.g. usd
        val currency_Symbol: String?
) : Parcelable