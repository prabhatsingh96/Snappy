package app.android.snappay.activity

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.view.View
import app.android.snappay.R
import app.android.snappay.databinding.ActivitySignUpEnterOtpBinding
import app.android.snappay.fragment.EditMobileNumberFragment
import app.android.snappay.model.request.SendOtpRequest
import app.android.snappay.model.request.VerifyOtpRequest
import app.android.snappay.model.response.SendOtpResponse
import app.android.snappay.model.response.VerifyOtpResponse
import app.android.snappay.util.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_sign_up_enter_otp.*

class SignUpEnterOtpActivity : BaseActivity(), View.OnClickListener, FragmentManager.OnBackStackChangedListener {

    private lateinit var binding: ActivitySignUpEnterOtpBinding
    private var verifyOtpDisposable: Disposable? = null
    private var resendOtpDisposable: Disposable? = null

    companion object {
        var TAG = SignUpEnterOtpActivity::class.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up_enter_otp)
        init(savedInstanceState)
        initControl()
    }

    override fun init(savedInstanceState: Bundle?) {
    }

    override fun initControl() {
        rl_root.setOnClickListener(this)
        tv_resend_otp.setOnClickListener(this)
        tv_edit_number.setOnClickListener(this)
        btn_submit.setOnClickListener(this)
        supportFragmentManager.addOnBackStackChangedListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.rl_root -> KeyboardUtils.hideKeyboard(this)
            R.id.tv_resend_otp -> {
                KeyboardUtils.hideKeyboard(this)
                updateUi(false)
                resendOtpDisposable = apiService.sendOtp(
                        sendOtpRequest = SendOtpRequest(
                                email_id = SharedPreferenceUtil.getInstance(this).email_id
                        )
                ).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { onResendOtpSuccess(it) },
                                { onResendOtpFailure(it) }
                        )
            }
            R.id.tv_edit_number -> {
                KeyboardUtils.hideKeyboard(this)
                supportFragmentManager.beginTransaction()
                        .replace(android.R.id.content, EditMobileNumberFragment.newInstance(), EditMobileNumberFragment.TAG)
                        .addToBackStack(EditMobileNumberFragment.TAG)
                        .commit()
            }
            R.id.btn_submit -> {
                KeyboardUtils.hideKeyboard(this)
                if (isValidInput()) {
                    updateUi(false)
                    verifyOtpDisposable = apiService.verifyOtp(
                            access_token = SharedPreferenceUtil.getInstance(this).access_token,
                            verifyOtpRequest = VerifyOtpRequest(et_otp.text.toString())
                    ).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    { onOtpVerifySuccess(it) },
                                    { onOtpVerifyFailure(it) }
                            )
                }
            }
        }
    }

    private fun onResendOtpSuccess(sendOtpResponse: SendOtpResponse) {
        updateUi(true)
        SnackbarUtils.displaySnackbar(binding.root, sendOtpResponse.message)
    }

    private fun onResendOtpFailure(throwable: Throwable) {
        updateUi(true)
        ErrorUtil.handlerGeneralError(this, binding.root, throwable)
    }

    private fun onOtpVerifySuccess(verifyOtpResponse: VerifyOtpResponse) {
        updateUi(true)
        if (verifyOtpResponse.response.is_varified.toBoolean) {
            SharedPreferenceUtil.getInstance(this).saveLoginResponse(verifyOtpResponse.response)

            if (!SharedPreferenceUtil.getInstance(this).is_pin_set.toBoolean) {
                startActivity(Intent(this, SetPinActivity::class.java))
                finish()
                return
            }
        }
    }

    private fun onOtpVerifyFailure(throwable: Throwable) {
        updateUi(true)
        ErrorUtil.handlerGeneralError(this, binding.root, throwable)
    }

    private fun updateUi(isEnable: Boolean) {
        main_progress_bar.visibility = if (isEnable) View.GONE else View.VISIBLE
        et_otp.isEnabled = isEnable
        tv_resend_otp.isEnabled = isEnable
        tv_edit_number.isEnabled = isEnable
        btn_submit.isEnabled = isEnable
    }

    fun isValidInput(): Boolean {
        if (et_otp.text.isEmpty()) {
            et_otp.requestFocus()
            ToastUtils.show(this, getString(R.string.error_empty_otp))
            return false
        }
        return true
    }

    override fun onBackStackChanged() {
        if (supportFragmentManager.backStackEntryCount == 0) {
            resetUi()
        }
    }

    private fun resetUi() {
        et_otp.text = null
    }

    override fun onDestroy() {
        verifyOtpDisposable?.dispose()
        resendOtpDisposable?.dispose()
        super.onDestroy()
    }

}