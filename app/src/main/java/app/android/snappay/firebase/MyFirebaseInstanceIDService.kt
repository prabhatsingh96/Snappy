package app.android.snappay.firebase

import app.android.snappay.connection.ApiService
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

class MyFirebaseInstanceIDService : FirebaseInstanceIdService() {

    private val apiService by lazy {
        ApiService.create()
    }

    companion object {
        var TAG = MyFirebaseInstanceIDService::class.simpleName
    }

    override fun onTokenRefresh() {
        FirebaseInstanceId.getInstance().token?.let {
            sendRegistrationToServer(it)
//            val intent = Intent(Constant.ACTION_FCM_TOKEN_REFRESHED)
//            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        }
    }

    private fun sendRegistrationToServer(token: String) {
//        apiService.setDeviceToken(PreferenceUtils.getAccessToken(this)
//                , PreferenceUtils.getId(this)
//                , token)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        { result -> onDeviceTokenUpdationSucess(result) },
//                        { error -> onDeviceTokenUpdationFailure(error) }
//                )
    }

//    private fun onDeviceTokenUpdationSucess(@Suppress("UNUSED_PARAMETER") result: UpdateDeviceTokenResponse) {
//    }
//
//    private fun onDeviceTokenUpdationFailure(error: Throwable?) {
//        Log.e(TAG, "Error in onDeviceTokenUpdationFailure(): ${error?.message}")
//    }

}