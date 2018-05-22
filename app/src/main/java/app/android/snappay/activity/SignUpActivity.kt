package app.android.snappay.activity

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import app.android.snappay.R
import app.android.snappay.constant.Constant
import app.android.snappay.databinding.ActivitySignUpBinding
import app.android.snappay.fragment.SignUpAdditionalDetailsFragment
import app.android.snappay.model.request.CheckValidUserRequest
import app.android.snappay.model.response.BaseResponse
import app.android.snappay.util.ErrorUtil
import app.android.snappay.util.KeyboardUtils
import app.android.snappay.util.ToastUtils
import app.android.snappay.util.isValidEmail
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : BaseActivity(), View.OnClickListener {

    private var backPressedTime: Long = 0
    private lateinit var binding: ActivitySignUpBinding

    companion object {
        var TAG = SignUpActivity::class.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        init(savedInstanceState)
        initControl()
    }

    override fun init(savedInstanceState: Bundle?) {
    }

    override fun initControl() {
        fl_root.setOnClickListener(this)
        ll_container.setOnClickListener(this)
        tv_login.setOnClickListener(this)
        btn_next.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fl_root -> KeyboardUtils.hideKeyboard(this)
            R.id.ll_container -> KeyboardUtils.hideKeyboard(this)
            R.id.tv_login -> {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            R.id.btn_next -> {
                KeyboardUtils.hideKeyboard(this)
                if (isValidInput()) {
                    updateUi(false)
                    apiService.checkIfValidUser(
                            CheckValidUserRequest(
                                    mobile_number = et_mobile.text.toString().toLong(),
                                    email_id = et_email.text.toString()
                            )
                    ).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    { onUserValidSuccess(it) },
                                    { onUserValidError(it) }
                            )
                }
            }
        }
    }

    private fun onUserValidSuccess(baseResponse: BaseResponse) {
        updateUi(true)
        supportFragmentManager.beginTransaction().add(
                android.R.id.content,
                SignUpAdditionalDetailsFragment.newInstance(
                        location = resources.getStringArray(R.array.location_options)[spinner_location.selectedItemPosition],
                        first_name = et_first_name.text.toString(),
                        last_name = et_last_name.text.toString(),
                        email_id = et_email.text.toString(),
                        country_code = ccp_country_code.selectedCountryCodeWithPlus,
                        mobile_number = et_mobile.text.toString().toLong(),
                        password = et_password.text.toString()
                ),
                SignUpAdditionalDetailsFragment.TAG
        )
                .addToBackStack(SignUpAdditionalDetailsFragment.TAG)
                .commit()
    }

    private fun onUserValidError(throwable: Throwable) {
        updateUi(true)
        ErrorUtil.handlerGeneralError(this, fl_root, throwable)
    }

    private fun updateUi(isEnable: Boolean) {
        main_progress_bar.visibility = if (isEnable) View.GONE else View.VISIBLE
        spinner_location.isEnabled = isEnable
        et_first_name.isEnabled = isEnable
        et_last_name.isEnabled = isEnable
        et_email.isEnabled = isEnable
        ccp_country_code.isEnabled = isEnable
        et_mobile.isEnabled = isEnable
        et_password.isEnabled = isEnable
        et_confirm_password.isEnabled = isEnable
        tv_login.isEnabled = isEnable
        btn_next.isEnabled = isEnable
    }

    fun isValidInput(): Boolean {
        if (et_first_name.text.isEmpty()) {
            et_first_name.requestFocus()
            ToastUtils.show(this, getString(R.string.error_empty_first_name))
            return false
        }

        if (et_last_name.text.isEmpty()) {
            et_last_name.requestFocus()
            ToastUtils.show(this, getString(R.string.error_empty_last_name))
            return false
        }

        if (et_email.text.isEmpty()) {
            et_email.requestFocus()
            ToastUtils.show(this, getString(R.string.error_empty_email))
            return false
        }


        if (!et_email.text.toString().isValidEmail) {
            et_email.requestFocus()
            ToastUtils.show(this, getString(R.string.error_invalid_email))
            return false
        }

        if (et_mobile.text.isEmpty()) {
            et_mobile.requestFocus()
            ToastUtils.show(this, getString(R.string.error_empty_mobile))
            return false
        }

        if (!TextUtils.isDigitsOnly(et_mobile.text)) {
            et_mobile.requestFocus()
            ToastUtils.show(this, getString(R.string.error_invalid_mobile))
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

        if (et_confirm_password.text.isEmpty()) {
            et_confirm_password.requestFocus()
            ToastUtils.show(this, getString(R.string.error_empty_confirm_password))
            return false
        }

        if (et_confirm_password.text.count() < resources.getInteger(R.integer.min_password_length)) {
            et_confirm_password.requestFocus()
            ToastUtils.show(this, String.format(getString(R.string.error_short_password), resources.getInteger(R.integer.min_password_length)))
            return false
        }

        if (!et_confirm_password.text.toString().equals(et_password.text.toString())) {
            et_confirm_password.requestFocus()
            ToastUtils.show(this, getString(R.string.error_password_not_matched))
            return false
        }

        return true
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            super.onBackPressed()
            return
        }

        if (backPressedTime + Constant.BACK_PRESS_TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed()
            return
        } else {
            ToastUtils.show(applicationContext, getString(R.string.message_double_tap_to_exit))
        }
        backPressedTime = System.currentTimeMillis()
    }

}
