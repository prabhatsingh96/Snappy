package app.android.snappay.model.request

data class MatchSecurityQuestionRequest(
        // either mobile_number or email_id is null
        val mobile_number: Long? = null,
        val email_id: String? = null,
        val question1_answer: String,
        val question2_answer: String
)
