package app.android.snappay.model.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserBean(
        val id: String,
        val email_id: String,
//        val password: String,
        val country_code: String,
        val mobile_number: Long,
        val first_name: String,
        val last_name: String,
        val access_token: String,
        val device_token: String,
//        val device_type: Int,
        val is_pin_set: Int,
//        val is_block: Int,
        val is_varified: Int,
//        val latitude: Double,
//        val longitude: Double,
//        val signup_type: Int,
//        val created_on: String,
        val varification_code: String,
        val pin: String?,

        // nullable as sometime it's not coming in response... And We are using a generic one.
        // Additional Details (comes from sign up)
        val bank_name: String?,
        val account_name: String?,
        val iban_code: String?,
        val routing_number: String?
) : Parcelable {

    val fullName: String
        get() {
            return "$first_name $last_name"
        }
}
