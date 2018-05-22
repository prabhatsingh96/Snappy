package app.android.snappay.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import app.android.snappay.R
import app.android.snappay.databinding.ActivityForgotPasswordBinding
import app.android.snappay.fragment.ForgotPasswordResetOptionFragment
import app.android.snappay.model.request.CheckUserExistRequest
import app.android.snappay.model.response.CheckUserExistResponse
import app.android.snappay.util.ErrorUtil
import app.android.snappay.util.KeyboardUtils
import app.android.snappay.util.ToastUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityForgotPasswordBinding
    private var checkIfUserExistDisposable: Disposable? = null

    companion object {
        var TAG = ForgotPasswordActivity::class.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password)
        init(savedInstanceState)
        initControl()
    }

    override fun init(savedInstanceState: Bundle?) {

    }

    override fun initControl() {
        fl_root.setOnClickListener(this)
        iv_back.setOnClickListener(this)
        btn_next.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fl_root -> KeyboardUtils.hideKeyboard(this)
            R.id.iv_back -> onBackPressed()
            R.id.btn_next -> {
                KeyboardUtils.hideKeyboard(this)
                if (isValidInput()) {
                    updateUi(false)

                    checkIfUserExistDisposable = apiService.checkIfUserExist(
                            CheckUserExistRequest(
                                    mobile_number = if (TextUtils.isDigitsOnly(et_email_mobile.text.toString())) et_email_mobile.text.toString().toLong() else null,
                                    email_id = if (TextUtils.isDigitsOnly(et_email_mobile.text.toString())) null else et_email_mobile.text.toString()
                            )
                    ).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    { onCheckIfUserExistSuccess(it) },
                                    { onCheckIfUserExistError(it) }
                            )
                }
            }
        }
    }

    private fun onCheckIfUserExistSuccess(@Suppress("UNUSED_PARAMETER") checkUserExistResponse: CheckUserExistResponse) {
        updateUi(true)
        supportFragmentManager
                .beginTransaction()
                .replace(android.R.id.content, ForgotPasswordResetOptionFragment.newInstance(
                        mobile_number = if (TextUtils.isDigitsOnly(et_email_mobile.text)) et_email_mobile.text.toString().toLong() else null,
                        email_id = if (TextUtils.isDigitsOnly(et_email_mobile.text)) null else et_email_mobile.text.toString()
                ), ForgotPasswordResetOptionFragment.TAG)
                .addToBackStack(ForgotPasswordResetOptionFragment.TAG)
                .commit()

    }

    private fun onCheckIfUserExistError(throwable: Throwable) {
        updateUi(true)
        ErrorUtil.handlerGeneralError(this, fl_root, throwable)
    }

    private fun updateUi(isEnable: Boolean) {
        main_progress_bar.visibility = if (isEnable) View.GONE else View.VISIBLE
        iv_back.isEnabled = isEnable
        et_email_mobile.isEnabled = isEnable
        btn_next.isEnabled = isEnable
    }

    fun isValidInput(): Boolean {
        if (et_email_mobile.text.isEmpty()) {
            et_email_mobile.requestFocus()
            ToastUtils.show(this, getString(R.string.error_empty_email_mobile))
            return false
        }
        return true
    }

    override fun onDestroy() {
        checkIfUserExistDisposable?.dispose()
        super.onDestroy()
    }
}