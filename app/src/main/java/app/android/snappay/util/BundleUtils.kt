package app.android.snappay.util

import android.content.Intent
import android.os.Bundle
import android.util.Log

object BundleUtils {
    val TAG = BundleUtils::class.simpleName

    fun printIntentData(tag: String?, intent: Intent?) {
        Log.d(tag ?: TAG, "printIntentData intent?.data: ${intent?.data}")
        printBundleData(tag, intent?.extras)
    }

    fun printBundleData(tag: String?, bundle: Bundle?) {
        Log.d(TAG, "printBundleData bundle: $bundle")
    }
}