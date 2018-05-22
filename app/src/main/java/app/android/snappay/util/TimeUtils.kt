package app.android.snappay.util

import android.content.Context
import app.android.snappay.R
import java.util.*

object TimeUtils {

    fun getGreetingMessage(context: Context): String {
        val calendar = Calendar.getInstance()
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        when (hourOfDay) {
            in 0..3 -> return context.getString(R.string.good_night)
            in 4..11 -> return context.getString(R.string.good_morning)
            in 12..15 -> return context.getString(R.string.good_after_noon)
            in 16..20 -> return context.getString(R.string.good_evening)
            in 21..23 -> return context.getString(R.string.good_night)
        }
        return ""
    }
}