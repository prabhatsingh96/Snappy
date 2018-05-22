package app.android.snappay.model.request

import com.google.gson.annotations.SerializedName

data class SendMoneyRequest(
        val receiver_user_id: String,
        val amount: Double,
        val currency_type: String,
        @SerializedName("key")
        val payWithWallet: Int,
        val service_charge: Int = 10, /*Percentage value*/
        val sgst: Int = 5, /*Percentage value*/
        val card_id: String?,
        val note: String?
)
