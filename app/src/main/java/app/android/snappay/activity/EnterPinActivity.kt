package app.android.snappay.activity

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import app.android.snappay.R
import app.android.snappay.databinding.ActivityEnterPinBinding
import app.android.snappay.model.request.VerifyPinRequest
import app.android.snappay.model.response.VerifyPinResponse
import app.android.snappay.util.ErrorUtil
import app.android.snappay.util.KeyboardUtils
import app.android.snappay.util.SharedPreferenceUtil
import app.android.snappay.util.ToastUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_enter_pin.*

class EnterPinActivity : BaseActivity(), View.OnClickListener {

    lateinit var binding: ActivityEnterPinBinding
    private var loginDisposable: Disposable? = null

    companion object {
        var TAG = SetPinActivity::class.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_enter_pin)
        init(savedInstanceState)
        initControl()
    }

    override fun init(savedInstanceState: Bundle?) {

    }

    override fun initControl() {
        fl_root.setOnClickListener(this)
        btn_submit.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fl_root -> KeyboardUtils.hideKeyboard(this)
            R.id.btn_submit -> {
                KeyboardUtils.hideKeyboard(this)
                if (isValidInput()) {

                    updateUi(false)
                    loginDisposable = apiService.verifyPin(
                            access_token = SharedPreferenceUtil.getInstance(this).access_token,
                            verifyPinRequest = VerifyPinRequest(
                                    pin = et_pin.text.toString()
                            )
                    ).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    { onPinVerifiedSuccess(it) },
                                    { onPinVerifiedError(it) }
                            )
                }
            }
        }
    }

    private fun updateUi(isEnable: Boolean) {
        main_progress_bar.visibility = if (isEnable) View.GONE else View.VISIBLE
        et_pin.isEnabled = isEnable
        btn_submit.isEnabled = isEnable
    }

    private fun onPinVerifiedSuccess(verifyPinResponse: VerifyPinResponse) {
        updateUi(true)
        SharedPreferenceUtil.getInstance(this).saveLoginResponse(verifyPinResponse.response)
        startActivity(Intent(this, HomeActivity::class.java))
    }

    private fun onPinVerifiedError(throwable: Throwable) {
        updateUi(true)
        ErrorUtil.handlerGeneralError(this, fl_root, throwable)
    }

    fun isValidInput(): Boolean {
        if (et_pin.text.isEmpty()) {
            et_pin.requestFocus()
            ToastUtils.show(this, getString(R.string.error_empty_pin))
            return false
        }
        if (et_pin.text.count() != resources.getInteger(R.integer.default_pin_length)) {
            et_pin.requestFocus()
            ToastUtils.show(this, String.format(getString(R.string.error_invalid_pin_length), resources.getInteger(R.integer.default_pin_length)))
            return false
        }
        return true
    }

    override fun onDestroy() {
        loginDisposable?.dispose()
        super.onDestroy()
    }

}