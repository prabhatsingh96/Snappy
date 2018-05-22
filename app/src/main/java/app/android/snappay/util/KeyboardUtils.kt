package app.android.snappay.util

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

object KeyboardUtils {
    @Suppress("unused")
    val TAG = KeyboardUtils::class.simpleName

    fun hideKeyboard(context: Context?) {
        if (context is Activity) {
            val focusedView = context.currentFocus
            val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(focusedView?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }
}