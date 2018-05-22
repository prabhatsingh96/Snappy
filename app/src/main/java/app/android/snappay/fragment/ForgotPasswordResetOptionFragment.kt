package app.android.snappay.fragment

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.android.snappay.R
import app.android.snappay.activity.BaseActivity
import app.android.snappay.constant.BundleConstant
import app.android.snappay.databinding.FragmentForgotPasswordResetOptionBinding
import app.android.snappay.model.request.BaseEmailOrMobileRequest
import app.android.snappay.model.request.SendOtpRequest
import app.android.snappay.model.response.SecurityQuestionResponse
import app.android.snappay.model.response.SendOtpResponse
import app.android.snappay.util.ErrorUtil
import app.android.snappay.util.KeyboardUtils
import app.android.snappay.util.ToastUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_forgot_password_reset_option.*

class ForgotPasswordResetOptionFragment : BaseFragment(), View.OnClickListener {

    private lateinit var binding: FragmentForgotPasswordResetOptionBinding
    private var sendOtpDisposable: Disposable? = null
    private var securityQuesDisposable: Disposable? = null

    private val mobile_number: Long?
        get() = arguments?.getLong(BundleConstant.MOBILE_NUMBER)

    private val email_id: String?
        get() = arguments?.getString(BundleConstant.EMAIL_ID)

    companion object {
        @Suppress("unused")
        val TAG = ForgotPasswordResetOptionFragment::class.simpleName

        fun newInstance(
                mobile_number: Long?,
                email_id: String?
        ) = ForgotPasswordResetOptionFragment().apply {
            arguments = Bundle().apply {
                mobile_number?.let { putLong(BundleConstant.MOBILE_NUMBER, it) }
                putString(BundleConstant.EMAIL_ID, email_id)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_forgot_password_reset_option, container, false)
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
        btn_next.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fl_root -> KeyboardUtils.hideKeyboard(context)
            R.id.iv_back -> activity?.onBackPressed()
            R.id.btn_next -> {
                when (spinner_options.selectedItemPosition) {
                    0 -> ToastUtils.show(context, getString(R.string.error_invalid_option))
                    1 -> {
                        updateUi(false)
                        securityQuesDisposable = (activity as BaseActivity).apiService.getSecurityQuestions(
                                baseEmailOrMobileRequest = BaseEmailOrMobileRequest(
                                        mobile_number = mobile_number,
                                        email_id = email_id
                                )
                        ).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        { onGetSecurityQuesSuccess(it) },
                                        { onGetSecurityQuesFailure(it) }
                                )
                    }
                    2 -> {
                        updateUi(false)
                        sendOtpDisposable = (activity as BaseActivity).apiService.sendOtp(
                                sendOtpRequest = SendOtpRequest(
                                        mobile_number = mobile_number,
                                        email_id = email_id
                                )
                        ).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        { onSendOtpSuccess(it) },
                                        { onSendOtpFailure(it) }
                                )
                    }
                }
            }
        }
    }

    private fun onGetSecurityQuesSuccess(securityQuestionResponse: SecurityQuestionResponse) {
        updateUi(true)
        activity?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(android.R.id.content, ForgotPasswordSecurityQuesFragment.newInstance(
                        mobile_number,
                        email_id,
                        securityQuestionResponse.response
                ), ForgotPasswordSecurityQuesFragment.TAG)
                ?.addToBackStack(ForgotPasswordSecurityQuesFragment.TAG)
                ?.commit()
    }

    private fun onGetSecurityQuesFailure(throwable: Throwable) {
        updateUi(true)
        ErrorUtil.handlerGeneralError(context, binding.root, throwable)
    }

    private fun onSendOtpSuccess(sendOtpResponse: SendOtpResponse) {
        updateUi(true)
        ToastUtils.show(context, sendOtpResponse.message)
        activity?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(
                        android.R.id.content,
                        ForgotPasswordEnterOtpFragment.newInstance(
                                email_id = sendOtpResponse.response.email_id,
                                mobile_number = sendOtpResponse.response.mobile_number
                        ),
                        ForgotPasswordEnterOtpFragment.TAG
                )
                ?.addToBackStack(ForgotPasswordEnterOtpFragment.TAG)
                ?.commit()
    }

    private fun onSendOtpFailure(throwable: Throwable) {
        updateUi(true)
        ErrorUtil.handlerGeneralError(context, binding.root, throwable)
    }

    private fun updateUi(isEnable: Boolean) {
        main_progress_bar.visibility = if (isEnable) View.GONE else View.VISIBLE
        iv_back.isEnabled = isEnable
        spinner_options.isEnabled = isEnable
        btn_next.isEnabled = isEnable
    }

    override fun onStart() {
        super.onStart()
        resetUi()
    }

    private fun resetUi() {
        spinner_options.setSelection(0)
    }

    override fun onDestroy() {
        sendOtpDisposable?.dispose()
        super.onDestroy()
    }
}


