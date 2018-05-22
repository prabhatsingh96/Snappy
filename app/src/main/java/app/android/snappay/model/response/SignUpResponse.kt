package app.android.snappay.model.response

import app.android.snappay.model.bean.UserBean

data class SignUpResponse(
        val message: String,
        val response: UserBean
)