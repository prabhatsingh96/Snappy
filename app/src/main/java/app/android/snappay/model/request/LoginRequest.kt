package app.android.snappay.model.request

import app.android.snappay.util.EnumUtils

data class LoginRequest(
        // either mobile_number or email_id is null
        val mobile_number: Long?,
        val email_id: String?,
        val password: String,
        val device_type: String = EnumUtils.DeviceType.ANDROID.value,
        val device_token: String? = "n/a",
        val latitude: Float? = 0.1f,
        val longitude: Float? = 0.1f
)
