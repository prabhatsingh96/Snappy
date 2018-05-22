package app.android.snappay.model.request

data class CheckValidUserRequest(
        val mobile_number: Long,
        val email_id: String
)