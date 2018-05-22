package app.android.snappay.model.response

data class SearchUserRequest(
        val first_name: String? = null,
        val mobile_number: Long? = null,
        val email_id: String? = null
)
