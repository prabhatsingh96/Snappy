package app.android.snappay.util

object EnumUtils {
    val TAG = EnumUtils::class.simpleName

    enum class DeviceType(val value: String) {
        IOS("1"), ANDROID("2")
    }
}