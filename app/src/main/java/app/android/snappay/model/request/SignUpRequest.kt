package app.android.snappay.model.request

import app.android.snappay.BuildConfig
import app.android.snappay.util.EnumUtils

data class SignUpRequest(
        val location: String,

        val country_code: String?,
        val mobile_number: Long?,
        val email_id: String?,
        val password: String,
        val device_type: String = EnumUtils.DeviceType.ANDROID.value,
        val device_token: String? = "n/a",
        val latitude: Float? = 0.1f,
        val longitude: Float? = 0.1f,
        val user_type: String = BuildConfig.USER_TYPE,

        // additional details
        val first_name: String,
        val last_name: String,
        val bank_name: String,
        val account_name: String,
        val iban_code: String,
        val swift_code: String,
        val routing_number: String,
        val question1_id: String,
        val question2_id: String,
        val question1_answer: String,
        val question2_answer: String

)
