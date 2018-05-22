package app.android.snappay.model.request

data class AddCardRequest(
        val card_name: String,
        val card_number: Long,
        val mm: Int,
        val yy: String,
        val cvv: Int,
        val prefrence: Int = 0
)
