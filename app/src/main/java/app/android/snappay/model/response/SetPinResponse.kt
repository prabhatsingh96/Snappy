package app.android.snappay.model.response

import app.android.snappay.model.bean.UserBean

data class SetPinResponse(
        val message: String,
        val response: UserBean
)
