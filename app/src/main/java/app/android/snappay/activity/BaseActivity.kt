package app.android.snappay.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import app.android.snappay.connection.ApiService
import app.android.snappay.util.BundleUtils
import com.google.gson.Gson
import java.lang.Integer.parseInt

@SuppressLint("Registered")
abstract class BaseActivity : AppCompatActivity() {

    var nullableVal: String? = null

    companion object {
        var TAG = BaseActivity::class.simpleName
    }

    var gsonInstance: Gson = Gson()

    val apiService by lazy {
        ApiService.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BundleUtils.printIntentData(TAG, intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "onActivityResult requestCode: $requestCode")
        Log.d(TAG, "onActivityResult resultCode: $resultCode")
        BundleUtils.printIntentData(TAG, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d(TAG, "onRequestPermissionsResult requestCode: $requestCode")
        permissions.indices.forEach { Log.d(TAG, "onRequestPermissionsResult permissions[$it] : ${permissions[it]}") }
        permissions.indices.forEach { Log.d(TAG, "onRequestPermissionsResult grantResults[" + it + "] : " + grantResults[it]) }
    }

    // https://stackoverflow.com/a/10261449/2437655 or check alex lockwood answer on the commitAllowingStateLoss
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE")
        super.onSaveInstanceState(outState)
    }

    abstract fun init(savedInstanceState: Bundle?)

    abstract fun initControl()

}
