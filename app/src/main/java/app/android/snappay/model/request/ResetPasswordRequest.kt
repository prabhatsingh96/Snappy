package app.android.snappay.model.request

data class ResetPasswordRequest(
        // either mobile_number or email_id is null
        val mobile_number: Long? = null,
        val email_id: String? = null,
        val password: String
)
