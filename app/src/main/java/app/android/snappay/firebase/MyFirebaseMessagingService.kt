package app.android.snappay.firebase

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import app.android.snappay.util.SharedPreferenceUtil
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private val TAG = MyFirebaseMessagingService::class.simpleName
        const val NOTIFICATION_CHANNEL_ID = "general"
        const val NOTIFICATION_ID_KEY = "NOTIFICATION_ID"
    }

    private lateinit var notificationManager: NotificationManager

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // no login... -> no push...
        if (!SharedPreferenceUtil.getInstance(this).isLoggedIn) return

        try {
            if (remoteMessage.data.isNotEmpty()) {
                Log.i(TAG, "Message data payload: " + remoteMessage.data)
//                val gsonInstance = Gson()
//                val notificationBean = gsonInstance.fromJson(gsonInstance.toJsonTree(remoteMessage.data), NotificationBean::class.java)
//                NotificationUtils.sendNotification(this, notificationBean)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in onMessageReceived() : ${e.message}")
        }
    }
}