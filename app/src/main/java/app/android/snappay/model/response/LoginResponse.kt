package app.android.snappay.model.response

import app.android.snappay.model.bean.UserBean

data class LoginResponse(
        val message: String,
        val response: UserBean
)