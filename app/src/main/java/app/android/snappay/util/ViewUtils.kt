package app.android.snappay.util

import android.content.res.Resources

@Suppress("unused")
object ViewUtils {
    val TAG = ViewUtils::class.simpleName
}

val Int.toPx: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Int.toDp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()
