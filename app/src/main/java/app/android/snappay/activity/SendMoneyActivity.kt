package app.android.snappay.activity

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import app.android.snappay.R
import app.android.snappay.constant.BundleConstant
import app.android.snappay.databinding.ActivitySendMoneyBinding
import app.android.snappay.model.response.SearchUserRequest
import app.android.snappay.model.response.SearchUserResponse
import app.android.snappay.util.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_send_money.*

class SendMoneyActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySendMoneyBinding
    private var searchUserDisposable: Disposable? = null

    companion object {
        var TAG = SendMoneyActivity::class.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_send_money)
        init(savedInstanceState)
        initControl()
    }

    override fun init(savedInstanceState: Bundle?) {
        setSupportActionBar(toolbar)
    }

    override fun initControl() {
        fl_root.setOnClickListener(this)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        tv_proceed_to_send.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fl_root -> KeyboardUtils.hideKeyboard(this)
            R.id.tv_proceed_to_send -> {
                KeyboardUtils.hideKeyboard(this)
                if (isValidInput()) {
                    updateUi(false)
                    searchUserDisposable = apiService.searchUser(
                            access_token = SharedPreferenceUtil.getInstance(this).access_token,
                            searchUserRequest = SearchUserRequest(
                                    mobile_number = if (TextUtils.isDigitsOnly(et_name_email_mobile.text.trim().toString())) et_name_email_mobile.text.trim().toString().toLong() else null,
                                    email_id = if (TextUtils.isDigitsOnly(et_name_email_mobile.text.trim().toString())) null else et_name_email_mobile.text.trim().toString()
                            )
                    ).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    { onSearchUserSuccess(it) },
                                    { onSearchUserError(it) }
                            )
                }
            }
        }
    }

    private fun updateUi(isEnable: Boolean) {
        main_progress_bar.visibility = if (isEnable) View.GONE else View.VISIBLE
        et_name_email_mobile.isEnabled = isEnable
        tv_proceed_to_send.isEnabled = isEnable
    }

    private fun onSearchUserSuccess(@Suppress("UNUSED_PARAMETER") searchUserResponse: SearchUserResponse) {
        updateUi(true)
        startActivity(
                Intent(this, EnterAmountActivity::class.java).apply {
                    putExtra(BundleConstant.USER_BEAN, searchUserResponse.response[0])
                }
        )
    }

    private fun onSearchUserError(throwable: Throwable) {
        updateUi(true)
        ErrorUtil.handlerGeneralError(this, fl_root, throwable)
    }

    fun isValidInput(): Boolean {
        if (et_name_email_mobile.text.trim().isEmpty()) {
            et_name_email_mobile.requestFocus()
            ToastUtils.show(this, getString(R.string.error_empty_name_email_mobile))
            return false
        }

        if (
                (et_name_email_mobile.text.trim().toString().isValidEmail && SharedPreferenceUtil.getInstance(this).email_id == et_name_email_mobile.text.trim().toString())
                or
                (TextUtils.isDigitsOnly(et_name_email_mobile.text.trim()) && SharedPreferenceUtil.getInstance(this).mobile_number == et_name_email_mobile.text.trim().toString().toLong())
        ) {
            et_name_email_mobile.requestFocus()
            ToastUtils.show(this, getString(R.string.error_same_payee))
            return false
        }
        return true
    }

    override fun onDestroy() {
        searchUserDisposable?.dispose()
        super.onDestroy()
    }
}