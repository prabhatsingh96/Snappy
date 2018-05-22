package app.android.snappay.fragment

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.android.snappay.R
import app.android.snappay.activity.BaseActivity
import app.android.snappay.constant.BundleConstant
import app.android.snappay.databinding.FragmentResetPasswordBinding
import app.android.snappay.model.request.ResetPasswordRequest
import app.android.snappay.model.response.ResetPasswordResponse
import app.android.snappay.util.ErrorUtil
import app.android.snappay.util.KeyboardUtils
import app.android.snappay.util.ToastUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_reset_password.*

class ResetPasswordFragment : BaseFragment(), View.OnClickListener {

    private lateinit var binding: FragmentResetPasswordBinding
    private var resetPasswordDisposable: Disposable? = null

    private val mobile_number: Long?
        get() = arguments?.getLong(BundleConstant.MOBILE_NUMBER)

    private val email_id: String?
        get() = arguments?.getString(BundleConstant.EMAIL_ID)

    companion object {
        @Suppress("unused")
        val TAG = ResetPasswordFragment::class.simpleName

        fun newInstance(
                mobile_number: Long?,
                email_id: String?
        ) = ResetPasswordFragment().apply {
            arguments = Bundle().apply {
                mobile_number?.let { putLong(BundleConstant.MOBILE_NUMBER, it) }
                putString(BundleConstant.EMAIL_ID, email_id)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reset_password, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
        initControl()
    }

    override fun init() {

    }

    override fun initControl() {
        fl_root.setOnClickListener(this)
        iv_back.setOnClickListener(this)
        btn_submit.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fl_root -> KeyboardUtils.hideKeyboard(context)
            R.id.iv_back -> activity?.onBackPressed()
            R.id.btn_submit -> {
                KeyboardUtils.hideKeyboard(context)
                if (isValidInput()) {
                    updateUi(false)
                    resetPasswordDisposable = (activity as BaseActivity).apiService.resetPassword(
                            ResetPasswordRequest(
                                    mobile_number = mobile_number,
                                    email_id = email_id,
                                    password = et_password.text.toString()
                            )
                    ).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    { onResetPasswordSuccess(it) },
                                    { onResetPasswordError(it) }
                            )
                }
            }
        }
    }

    private fun updateUi(isEnable: Boolean) {
        main_progress_bar.visibility = if (isEnable) View.GONE else View.VISIBLE
        iv_back.isEnabled = isEnable
        et_password.isEnabled = isEnable
        et_confirm_password.isEnabled = isEnable
        btn_submit.isEnabled = isEnable
    }

    private fun onResetPasswordSuccess(@Suppress("UNUSED_PARAMETER") resetPasswordResponse: ResetPasswordResponse) {
        updateUi(true)
        ToastUtils.show(context, getString(R.string.message_password_changed))
        activity?.finish()
    }

    private fun onResetPasswordError(throwable: Throwable) {
        updateUi(true)
        ErrorUtil.handlerGeneralError(context, fl_root, throwable)
    }

    fun isValidInput(): Boolean {

        if (et_password.text.isEmpty()) {
            et_password.requestFocus()
            ToastUtils.show(context, getString(R.string.error_empty_new_password))
            return false
        }

        if (et_password.text.count() < resources.getInteger(R.integer.min_password_length)) {
            et_password.requestFocus()
            ToastUtils.show(context, String.format(getString(R.string.error_short_new_password), resources.getInteger(R.integer.min_password_length)))
            return false
        }

        if (et_confirm_password.text.isEmpty()) {
            et_confirm_password.requestFocus()
            ToastUtils.show(context, getString(R.string.error_empty_confirm_new_password))
            return false
        }

        if (et_confirm_password.text.count() < resources.getInteger(R.integer.min_password_length)) {
            et_confirm_password.requestFocus()
            ToastUtils.show(context, String.format(getString(R.string.error_short_new_confirm_password), resources.getInteger(R.integer.min_password_length)))
            return false
        }

        if (!et_confirm_password.text.toString().equals(et_password.text.toString())) {
            et_confirm_password.requestFocus()
            ToastUtils.show(context, getString(R.string.error_password_not_matched))
            return false
        }

        return true
    }

    override fun onStart() {
        super.onStart()
        resetUi()
    }

    private fun resetUi() {
        et_password.text = null
        et_confirm_password.text = null
    }

    override fun onDestroy() {
        resetPasswordDisposable?.dispose()
        super.onDestroy()
    }
}