package app.android.snappay.util

val Int.toBoolean: Boolean
    get() = this == 1

val Boolean.toInt: Int
    get() = if (this) 1 else 0

object Utils {

}