package app.android.snappay.util

import android.databinding.BindingAdapter
import android.graphics.Paint
import android.widget.TextView

/**
 * No need to use this function as they view is not as per the UI
 */
@BindingAdapter("underline")
fun setUnderline(textView: TextView, isUnderline: Boolean) {
    if (isUnderline) {
        textView.paintFlags = textView.paintFlags or Paint.UNDERLINE_TEXT_FLAG
    }
}

object BindingUtils {
    val TAG = BindingUtils::class.simpleName
}