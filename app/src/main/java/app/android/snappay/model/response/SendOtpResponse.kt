package app.android.snappay.model.response

import app.android.snappay.model.bean.UserBean

data class SendOtpResponse(
        val message: String,
        val response: UserBean
)