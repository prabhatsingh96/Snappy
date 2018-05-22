package app.android.snappay.model.response

import app.android.snappay.model.bean.UserBean

data class SearchUserResponse(
        val message: String,
        val response: List<UserBean>
)
