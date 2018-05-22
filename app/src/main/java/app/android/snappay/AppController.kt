package app.android.snappay

import android.support.multidex.MultiDexApplication
import com.crashlytics.android.Crashlytics
import com.facebook.stetho.Stetho
import io.fabric.sdk.android.Fabric

@Suppress("unused")
class AppController : MultiDexApplication() {
    companion object {
        private val TAG = AppController::class.simpleName
    }

    override fun onCreate() {
        super.onCreate()
        Fabric.with(this, Crashlytics())
        Stetho.initializeWithDefaults(applicationContext)
    }
}
