package app.android.snappay.util

import android.content.Context
import android.net.ConnectivityManager

object NetworkUtils {
    val TAG = NetworkUtils::class.simpleName

    fun isOnline(context: Context): Boolean {
        try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.activeNetworkInfo
            //should check null because in airplane mode it will be null
            return netInfo != null && netInfo.isConnected
        } catch (e: NullPointerException) {
            e.printStackTrace()
            return false
        }
    }
}
