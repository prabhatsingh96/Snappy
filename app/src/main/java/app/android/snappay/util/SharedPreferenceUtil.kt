package app.android.snappay.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import app.android.snappay.constant.Constant
import app.android.snappay.model.bean.UserBean

class SharedPreferenceUtil
private constructor(val context: Context) {
    val TAG = SharedPreferenceUtil::class.java.simpleName!!

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(Constant.PREFRENCE_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var instance: SharedPreferenceUtil? = null

        fun getInstance(ctx: Context): SharedPreferenceUtil {
            if (instance == null) {
                instance = SharedPreferenceUtil(ctx)
            }
            return instance!!
        }
    }

    var id: String
        get() = sharedPreferences["id", ""]!!
        set(value) = sharedPreferences.set("id", value)

    var email_id: String
        get() = sharedPreferences["email_id", ""]!!
        set(value) = sharedPreferences.set("email_id", value)

    var country_code: String
        get() = sharedPreferences["country_code", ""]!!
        set(value) = sharedPreferences.set("country_code", value)

    var mobile_number: Long
        get() = sharedPreferences["mobile_number", 0]!!
        set(value) = sharedPreferences.set("mobile_number", value)

    val fullName: String
        get() {
            return "$first_name $last_name"
        }

    var first_name: String
        get() = sharedPreferences["first_name", ""]!!
        set(value) = sharedPreferences.set("first_name", value)

    var last_name: String
        get() = sharedPreferences["last_name", ""]!!
        set(value) = sharedPreferences.set("last_name", value)

    var access_token: String
        get() = sharedPreferences["access_token", ""]!!
        set(value) = sharedPreferences.set("access_token", value)

    var device_token: String
        get() = sharedPreferences["device_token", ""]!!
        set(value) = sharedPreferences.set("device_token", value)

    var is_pin_set: Int
        get() = sharedPreferences["is_pin_set", 0]!!
        set(value) = sharedPreferences.set("is_pin_set", value)

    var is_varified: Int
        get() = sharedPreferences["is_varified", 0]!!
        set(value) = sharedPreferences.set("is_varified", value)

    var varification_code: String
        get() = sharedPreferences["varification_code", ""]!!
        set(value) = sharedPreferences.set("varification_code", value)

    var pin: String
        get() = sharedPreferences["pin", ""]!!
        set(value) = sharedPreferences.set("pin", value)

    var isLoggedIn: Boolean
        get() = sharedPreferences["is_logged_in", false]!!
        set(value) = sharedPreferences.set("is_logged_in", value)

    fun saveLoginResponse(userBean: UserBean) {
        id = userBean.id
        email_id = userBean.email_id
        country_code = userBean.country_code
        mobile_number = userBean.mobile_number
        first_name = userBean.first_name
        last_name = userBean.last_name
        access_token = userBean.access_token
        device_token = userBean.device_token
        is_pin_set = userBean.is_pin_set
        is_varified = userBean.is_varified
        varification_code = userBean.varification_code
        userBean.pin?.let { pin = it }
    }

    /**
     * puts a key value pair in shared prefs if doesn't exists, otherwise updates value on given [key]
     */
    operator fun SharedPreferences.set(key: String, value: Any?) {
        when (value) {
            is String? -> edit({ it.putString(key, value) })
            is Int -> edit({ it.putInt(key, value) })
            is Boolean -> edit({ it.putBoolean(key, value) })
            is Float -> edit({ it.putFloat(key, value) })
            is Long -> edit({ it.putLong(key, value) })
            else -> Log.e(TAG, "Setting shared pref failed for key: $key and value: $value ")
        }
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = this.edit()
        operation(editor)
        editor.apply()
    }

    /**
     * finds value on given key.
     * [T] is the type of value
     * @param defaultValue optional default value - will take null for strings, false for bool and -1 for numeric values if [defaultValue] is not specified
     */
    inline operator fun <reified T : Any> SharedPreferences.get(key: String, defaultValue: T? = null): T? {
        return when (T::class) {
            String::class -> getString(key, defaultValue as? String) as T?
            Int::class -> getInt(key, defaultValue as? Int ?: -1) as T?
            Boolean::class -> getBoolean(key, defaultValue as? Boolean ?: false) as T?
            Float::class -> getFloat(key, defaultValue as? Float ?: -1f) as T?
            Long::class -> getLong(key, defaultValue as? Long ?: -1) as T?
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }

    fun deletePreferences() {
        editor.clear()
        editor.apply()
    }
}