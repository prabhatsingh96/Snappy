package app.android.snappay.util

import app.android.snappay.constant.Constant

val String.isValidEmail: Boolean
    get() = if (this.length < 3 || this.length > 265)
        false
    else {
        this.matches(Constant.EMAIL_PATTERN.toRegex())
    }

object ValidationUtils {
    val TAG = ValidationUtils::class.simpleName
}
