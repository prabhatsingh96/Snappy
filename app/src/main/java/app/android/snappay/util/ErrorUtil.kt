package app.android.snappay.util

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import app.android.snappay.activity.LoginActivity
import app.android.snappay.connection.ApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException

object ErrorUtil {
    val TAG = ErrorUtil::class.simpleName

    fun handlerGeneralError(context: Context?, view: View?, throwable: Throwable) {
        Log.e(TAG, "Error: ${throwable.message}")
        throwable.printStackTrace()

        if (context == null) return

        when (throwable) {
            is HttpException -> {
                try {
                    when (throwable.code()) {
                        401 -> {
                            SnackbarUtils.displayError(view, throwable)
                            logout(context)
                        }
                        else -> {
                            SnackbarUtils.displayError(view, throwable)
                        }
                    }
                } catch (exception: Exception) {
                    SnackbarUtils.somethingWentWrong(view)
                    exception.printStackTrace()
                }
            }
            is ConnectException -> SnackbarUtils.displayError(view, throwable)
            is SocketTimeoutException -> SnackbarUtils.displayError(view, throwable)
            else -> SnackbarUtils.somethingWentWrong(view)
        }
    }

    private fun logout(context: Context) {
        // preventing from unnessary error if occur however didn't find any
        try {
            if (context is LoginActivity) return
            ApiService.create().logout(
                    access_token = SharedPreferenceUtil.getInstance(context).access_token
            ).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {},
                            { forceLogout(context) },
                            { forceLogout(context) }
                    )
        } catch (e: Exception) {
        }
    }

    /**
     * Perform logout for both the success and error case (force logout)
     */
    private fun forceLogout(context: Context) {
        SharedPreferenceUtil.getInstance(context).deletePreferences()
        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

}