package app.android.snappay.model.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SecurityQuestionBean(
//        val id: String,
//        val user_id: String,
        val question1_id: Int,
        val question2_id: Int
) : Parcelable
