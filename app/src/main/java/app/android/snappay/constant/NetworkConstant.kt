package app.android.snappay.constant

interface NetworkConstant {
    companion object {
        const val BASE_URL = "http://18.188.215.228:3000/"

        const val USER_CHECK_IF_VALID_USER = "user/isValidUser"
        const val USER_SIGN_UP = "user/signup"
        const val USER_VERIFY_OTP = "user/verify/otp"
        const val USER_CHANGE_MOBILE_NUMBER = "user/ChangeMobileNumber"
        const val USER_SET_PIN = "user/setpin"
        /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
        const val USER_CHECK_IF_EXISTS = "user/checkUserExists"
        const val USER_GET_SECURITY_QUESTION = "user/getSecurityQuestion"
        const val USER_MATCH_SECURITY_QUES = "user/matchSecurityQuestions"
        const val USER_SEND_OTP = "user/sendOtp"
        const val USER_MATCH_OTP = "user/matchOtp"
        const val USER_RESET_PASSWORD = "user/resetPassword"
        /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
        const val USER_VERIFY_PIN = "user/pin_login"
        /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
        const val USER_LOGOUT = "user/logout"
        /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
        const val USER_UPLOAD_PROFILE_IMAGE = "user/uploadProfileImage"
        const val USER_REMOVE_PROFILE_IMAGE = "user/removeProfileImage"
        /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
        /*Send Money*/
        const val USER_SEARCH_USER = "user/searchUser"
        const val USER_WALLET_DETAILS = "/user/getwalletdetails"
        const val USER_ADD_CARD = "/user/addCardDetails"
        const val USER_GET_SUMMARY = "/user/getSummary"
        const val USER_SEND_MONEY = "/user/SendMoney"
        /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
        const val USER_DETAILS = "/user/getUserDetails"
    }
}