package app.android.snappay.util

import android.content.Context
import android.widget.Toast
import com.google.gson.JsonObject

object ToastUtils {

    @Suppress("unused")
    val TAG = ToastUtils::class.simpleName

    fun show(context: Context?, msg: String?, duration: Int = Toast.LENGTH_SHORT) {
        if (context == null) return
        if (msg == null) return
        Toast.makeText(context, msg, duration).show()
    }

    fun displayMessage(context: Context?, result: JsonObject) {
        try {
            show(context, result.get("content").asString)
        } catch (e: UnsupportedOperationException) {
            e.printStackTrace()
        }
    }

}

