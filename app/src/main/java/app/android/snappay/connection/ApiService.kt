package app.android.snappay.connection

import app.android.snappay.BuildConfig
import app.android.snappay.constant.NetworkConstant
import app.android.snappay.model.request.*
import app.android.snappay.model.response.*
import com.google.gson.GsonBuilder
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface ApiService {

    @POST(NetworkConstant.USER_SIGN_UP)
    fun signUp(
            @Body signupRequest: SignUpRequest
    ): Observable<SignUpResponse>

    @POST(BuildConfig.USER_LOGIN_ENDPOINT)
    fun login(
            @Body loginRequest: LoginRequest
    ): Observable<LoginResponse>

    @POST(NetworkConstant.USER_VERIFY_OTP)
    fun verifyOtp(
            @Header("access_token") access_token: String,
            @Body verifyOtpRequest: VerifyOtpRequest
    ): Observable<VerifyOtpResponse>

    @POST(NetworkConstant.USER_CHANGE_MOBILE_NUMBER)
    fun changeMobileNumber(
            @Header("access_token") access_token: String,
            @Body changeMobileNumberRequest: ChangeMobileNumberRequest
    ): Observable<ChangeMobileNumberResponse>

    @POST(NetworkConstant.USER_SET_PIN)
    fun setPin(
            @Header("access_token") access_token: String,
            @Body setPinRequest: SetPinRequest
    ): Observable<SetPinResponse>

    @POST(NetworkConstant.USER_SEND_OTP)
    fun sendOtp(
            @Body sendOtpRequest: SendOtpRequest
    ): Observable<SendOtpResponse>

    @POST(NetworkConstant.USER_MATCH_OTP)
    fun matchOtp(
            @Body matchOtpRequest: MatchOtpRequest
    ): Observable<MatchOtpResponse>

    @POST(NetworkConstant.USER_CHECK_IF_EXISTS)
    fun checkIfUserExist(
            @Body checkUserExistRequest: CheckUserExistRequest
    ): Observable<CheckUserExistResponse>

    @POST(NetworkConstant.USER_CHECK_IF_VALID_USER)
    fun checkIfValidUser(
            @Body checkValidUserRequest: CheckValidUserRequest
    ): Observable<BaseResponse>

    @POST(NetworkConstant.USER_GET_SECURITY_QUESTION)
    fun getSecurityQuestions(
            @Body baseEmailOrMobileRequest: BaseEmailOrMobileRequest
    ): Observable<SecurityQuestionResponse>

    @POST(NetworkConstant.USER_MATCH_SECURITY_QUES)
    fun matchSecurityQues(
            @Body matchSecurityQuestionRequest: MatchSecurityQuestionRequest
    ): Observable<MatchSecurityQuestionResponse>

    @POST(NetworkConstant.USER_RESET_PASSWORD)
    fun resetPassword(
            @Body resetPasswordRequest: ResetPasswordRequest
    ): Observable<ResetPasswordResponse>

    @POST(NetworkConstant.USER_VERIFY_PIN)
    fun verifyPin(
            @Header("access_token") access_token: String,
            @Body verifyPinRequest: VerifyPinRequest
    ): Observable<VerifyPinResponse>

    @POST(NetworkConstant.USER_LOGOUT)
    fun logout(
            @Header("access_token") access_token: String
    ): Observable<BaseResponse>

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    @Multipart
    @POST(NetworkConstant.USER_UPLOAD_PROFILE_IMAGE)
    fun uploadProfilePic(
            @Header("access_token") access_token: String,
            @Part() file: MultipartBody.Part
    ): Observable<UploadProfilePicResponse>

    @POST(NetworkConstant.USER_REMOVE_PROFILE_IMAGE)
    fun removeProfilePic(
            @Header("access_token") access_token: String
    ): Observable<BaseResponse>

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    @POST(NetworkConstant.USER_SEARCH_USER)
    fun searchUser(
            @Header("access_token") access_token: String,
            @Body searchUserRequest: SearchUserRequest
    ): Observable<SearchUserResponse>

    @POST(NetworkConstant.USER_WALLET_DETAILS)
    fun getWalletDetails(
            @Header("access_token") access_token: String
    ): Observable<WalletDetailResponse>

    @POST(NetworkConstant.USER_ADD_CARD)
    fun addCard(
            @Header("access_token") access_token: String,
            @Body addCardRequest: AddCardRequest
    ): Observable<AddCardResponse>

    @POST(NetworkConstant.USER_GET_SUMMARY)
    fun getSummary(
            @Header("access_token") access_token: String,
            @Body summaryRequest: SummaryRequest
    ): Observable<SummaryResponse>

    @POST(NetworkConstant.USER_SEND_MONEY)
    fun sendMoney(
            @Header("access_token") access_token: String,
            @Body sendMoneyRequest: SendMoneyRequest
    ): Observable<SendMoneyResponse>

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    @POST(NetworkConstant.USER_DETAILS)
    fun getUserDetails(
            @Header("access_token") access_token: String,
            @Body userDetailsRequest: UserDetailsRequest
    ): Observable<UserDetailsResponse>
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    companion object {
        fun create(
                baseUrl: String = NetworkConstant.BASE_URL,
                connectTimeoutInSec: Long = 10,
                readTimeoutInSec: Long = 30,
                writeTimeoutInSec: Long = 60
        ): ApiService {
            val client = OkHttpClient.Builder()
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            client.addInterceptor(loggingInterceptor)

            client.connectTimeout(connectTimeoutInSec, TimeUnit.SECONDS)
            client.readTimeout(readTimeoutInSec, TimeUnit.SECONDS)
            client.writeTimeout(writeTimeoutInSec, TimeUnit.SECONDS)
            val gson = GsonBuilder().setLenient().create()

            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .client(client.build())
                    .baseUrl(baseUrl)
                    .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}