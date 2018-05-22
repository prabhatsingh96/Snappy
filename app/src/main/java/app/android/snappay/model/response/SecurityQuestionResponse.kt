package app.android.snappay.model.response

import app.android.snappay.model.bean.SecurityQuestionBean

data class SecurityQuestionResponse(
        val message: String,
        val response: SecurityQuestionBean
)
