package app.android.snappay.fragment

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.android.snappay.R
import app.android.snappay.activity.BaseActivity
import app.android.snappay.constant.BundleConstant
import app.android.snappay.databinding.FragmentForgotPasswordEnterOtpBinding
import app.android.snappay.model.request.MatchOtpRequest
import app.android.snappay.model.request.SendOtpRequest
import app.android.snappay.model.response.MatchOtpResponse
import app.android.snappay.model.response.SendOtpResponse
import app.android.snappay.util.ErrorUtil
import app.android.snappay.util.KeyboardUtils
import app.android.snappay.util.SnackbarUtils
import app.android.snappay.util.ToastUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_forgot_password_enter_otp.*

class ForgotPasswordEnterOtpFragment : BaseFragment(), View.OnClickListener {

    private lateinit var binding: FragmentForgotPasswordEnterOtpBinding
    private var resendOtpDisposable: Disposable? = null
    private var matchOtpDisposable: Disposable? = null

    private val email_id: String
        get() = arguments?.getString(BundleConstant.EMAIL_ID) ?: ""

    private val mobile_number: Long
        get() = arguments?.getLong(BundleConstant.MOBILE_NUMBER) ?: 0

    companion object {
        @Suppress("unused")
        val TAG = ForgotPasswordEnterOtpFragment::class.simpleName

        fun newInstance(email_id: String, mobile_number: Long) = ForgotPasswordEnterOtpFragment().apply {
            arguments = Bundle().apply {
                putString(BundleConstant.EMAIL_ID, email_id)
                putLong(BundleConstant.MOBILE_NUMBER, mobile_number)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_forgot_password_enter_otp, container, false)
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
        tv_resend_otp.setOnClickListener(this)
        btn_next.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fl_root -> KeyboardUtils.hideKeyboard(context)
            R.id.iv_back -> activity?.onBackPressed()
            R.id.tv_resend_otp -> {
                KeyboardUtils.hideKeyboard(context)
                updateUi(false)
                resendOtpDisposable = (activity as BaseActivity).apiService.sendOtp(
                        sendOtpRequest = SendOtpRequest(
                                email_id = email_id
                        )
                ).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { onResendOtpSuccess(it) },
                                { onResendOtpFailure(it) }
                        )

            }
            R.id.btn_next -> {
                KeyboardUtils.hideKeyboard(context)
                if (isValidInput()) {
                    updateUi(false)
                    matchOtpDisposable = (activity as BaseActivity).apiService.matchOtp(
                            matchOtpRequest = MatchOtpRequest(
                                    mobile_number = mobile_number,
                                    email_id = email_id,
                                    varification_code = et_otp.text.toString()
                            )
                    ).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    { onMatchOtpSuccess(it) },
                                    { onMatchOtpFailure(it) }
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
        ErrorUtil.handlerGeneralError(context, binding.root, throwable)
    }

    private fun onMatchOtpSuccess(@Suppress("UNUSED_PARAMETER") matchOtpResponse: MatchOtpResponse) {
        updateUi(true)
        if (isVisible) {
            activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.replace(android.R.id.content, ResetPasswordFragment.newInstance(
                            mobile_number,
                            email_id
                    ), ResetPasswordFragment.TAG)
                    ?.addToBackStack(ResetPasswordFragment.TAG)
                    ?.commit()
        }
    }

    private fun onMatchOtpFailure(throwable: Throwable) {
        updateUi(true)
        ErrorUtil.handlerGeneralError(context, binding.root, throwable)
    }

    private fun updateUi(isEnable: Boolean) {
        main_progress_bar.visibility = if (isEnable) View.GONE else View.VISIBLE
        iv_back.isEnabled = isEnable
        et_otp.isEnabled = isEnable
        tv_resend_otp.isEnabled = isEnable
        btn_next.isEnabled = isEnable
    }

    fun isValidInput(): Boolean {
        if (et_otp.text.isEmpty()) {
            et_otp.requestFocus()
            ToastUtils.show(context, getString(R.string.error_empty_otp))
            return false
        }
        return true
    }

    override fun onStart() {
        super.onStart()
        resetUi()
    }

    private fun resetUi() {
        et_otp.text = null
    }

    override fun onDestroy() {
        matchOtpDisposable?.dispose()
        resendOtpDisposable?.dispose()
        super.onDestroy()
    }
}