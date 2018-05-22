package app.android.snappay.util

object NotificationUtils {
    val TAG = NotificationUtils::class.simpleName

//    fun sendNotification(
//            context: Context,
//            notificationBean: NotificationBean
//    ) {
//        try {
//            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            val intent = Intent(context, SplashActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            val pendingIntent = PendingIntent.getActivity(
//                    context,
//                    0 /* Request code */,
//                    intent,
//                    PendingIntent.FLAG_ONE_SHOT
//            )
//
//            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//            val notificationBuilder = NotificationCompat.Builder(context, MyFirebaseMessagingService.NOTIFICATION_CHANNEL_ID)
//                    .setSmallIcon(R.drawable.ic_notification_status_bar)
//                    .setContentTitle(notificationBean.getNotificationTitle(context))
//                    .setContentText(notificationBean.message)
//                    .setAutoCancel(true)
//                    .setChannelId(MyFirebaseMessagingService.NOTIFICATION_CHANNEL_ID)
//                    .setSound(defaultSoundUri)
//                    .setContentIntent(pendingIntent)
//                    .setPriority(NotificationCompat.PRIORITY_HIGH)
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                notificationManager.createNotificationChannel(
//                        NotificationChannel(
//                                MyFirebaseMessagingService.NOTIFICATION_CHANNEL_ID,
//                                context.getString(R.string.notification_channel_general),
//                                NotificationManager.IMPORTANCE_DEFAULT
//                        )
//                )
//            }
//            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
//        } catch (e: Exception) {
//            Log.e(TAG, "Error in sendNotification() : ${e.message}")
//        }
//    }

}