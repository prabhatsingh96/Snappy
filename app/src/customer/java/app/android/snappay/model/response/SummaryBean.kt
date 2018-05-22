package app.android.snappay.model.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SummaryBean(
//        val transection_id: String,
//        val sender_id: String,
//        val amount: Double,
//        val time: String,
//        val currency_type: String,
//        val service_charge: Double,
//        val sgst: Double,
//        val wallet_id: String,
//        val status: Int,
        val service_tax: Double,
        val gst: Double,
        val total_amount: Double,
        val wallet_deduct_amount: Double,
        val formatted_card_number: String?,
        val card_received_amount: Double
) : Parcelable
