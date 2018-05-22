package app.android.snappay.activity

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.text.TextUtils
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.CompoundButton
import app.android.snappay.R
import app.android.snappay.constant.Constant
import app.android.snappay.databinding.ActivityLoginBinding
import app.android.snappay.model.request.LoginRequest
import app.android.snappay.model.response.LoginResponse
import app.android.snappay.util.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity(), View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private var backPressedTime: Long = 0
    private lateinit var binding: ActivityLoginBinding
    private var loginDisposable: Disposable? = null

    companion object {
        var TAG = LoginActivity::class.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        init(savedInstanceState)
        initControl()
    }

    override fun init(savedInstanceState: Bundle?) {
    }

    override fun initControl() {
        ll_root.setOnClickListener(this)
        btn_hide_show.setOnCheckedChangeListener(this)
        tv_forgot_password.setOnClickListener(this)
        tv_sign_up.setOnClickListener(this)
        btn_login.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.ll_root -> KeyboardUtils.hideKeyboard(this)
            R.id.tv_forgot_password -> {
                startActivity(Intent(this, ForgotPasswordActivity::class.java))
                // no need to finish()
            }
            R.id.tv_sign_up -> {
                startActivity(Intent(this, SignUpActivity::class.java))
                finish()
            }
            R.id.btn_login -> {
                KeyboardUtils.hideKeyboard(this)
                if (isValidInput()) {
                    updateUi(false)
                    loginDisposable = apiService.login(
                            LoginRequest(
                                    mobile_number = if (TextUtils.isDigitsOnly(et_email_mobile.text.toString())) et_email_mobile.text.toString().toLong() else null,
                                    email_id = if (TextUtils.isDigitsOnly(et_email_mobile.text.toString())) null else et_email_mobile.text.toString(),
                                    password = et_password.text.toString()
                            )
                    ).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    { onLoginSuccess(it) },
                                    { onLoginError(it) }
                            )
                }
            }
        }
    }

    private fun updateUi(isEnable: Boolean) {
        main_progress_bar.visibility = if (isEnable) View.GONE else View.VISIBLE
        et_email_mobile.isEnabled = isEnable
        et_password.isEnabled = isEnable
        btn_hide_show.isEnabled = isEnable
        tv_forgot_password.isEnabled = isEnable
        tv_sign_up.isEnabled = isEnable
        btn_login.isEnabled = isEnable
    }

    private fun onLoginSuccess(loginResponse: LoginResponse) {
        updateUi(true)
        SharedPreferenceUtil.getInstance(this).saveLoginResponse(loginResponse.response)
        SharedPreferenceUtil.getInstance(this).isLoggedIn = true

        if (!SharedPreferenceUtil.getInstance(this).is_varified.toBoolean) {
            startActivity(Intent(this, SignUpEnterOtpActivity::class.java))
            finish()
            return
        }

        if (!SharedPreferenceUtil.getInstance(this).is_pin_set.toBoolean) {
            startActivity(Intent(this, SetPinActivity::class.java))
            finish()
            return
        }

        val intent = Intent(this, EnterPinActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun onLoginError(throwable: Throwable) {
        updateUi(true)
        ErrorUtil.handlerGeneralError(this, rl_root, throwable)
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        when (isChecked) {
            true -> {
                et_password.transformationMethod = null
            }
            false -> {
                et_password.transformationMethod = PasswordTransformationMethod()
            }
        }
    }

    fun isValidInput(): Boolean {
        if (et_email_mobile.text.isEmpty()) {
            et_email_mobile.requestFocus()
            ToastUtils.show(this, getString(R.string.error_empty_email_mobile))
            return false
        }

        if (et_password.text.isEmpty()) {
            et_password.requestFocus()
            ToastUtils.show(this, getString(R.string.error_empty_password))
            return false
        }

        if (et_password.text.count() < resources.getInteger(R.integer.min_password_length)) {
            et_password.requestFocus()
            ToastUtils.show(this, String.format(getString(R.string.error_short_password), resources.getInteger(R.integer.min_password_length)))
            return false
        }

        return true
    }

    override fun onBackPressed() {
        if (backPressedTime + Constant.BACK_PRESS_TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed()
            return
        } else {
            ToastUtils.show(applicationContext, getString(R.string.message_double_tap_to_exit))
        }
        backPressedTime = System.currentTimeMillis()
    }

    override fun onStart() {
        super.onStart()
        resetUi()
    }

    private fun resetUi() {
        et_email_mobile.text = null
        et_password.text = null
    }

    override fun onDestroy() {
        loginDisposable?.dispose()
        super.onDestroy()
    }
}