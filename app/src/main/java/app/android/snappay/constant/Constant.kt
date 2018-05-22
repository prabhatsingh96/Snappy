package app.android.snappay.constant

interface Constant {
    companion object {
        const val EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        const val DELAY_FOR_NEXT_SCREEN: Long = 2000
        const val BACK_PRESS_TIME_INTERVAL: Long = 2000

        /*SHARED PREF CONSTANTS*/
        const val PREFRENCE_NAME: String = "app_pref"

    }
}
