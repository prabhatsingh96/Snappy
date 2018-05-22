package app.android.snappay.fragment

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.android.snappay.R
import app.android.snappay.activity.BaseActivity
import app.android.snappay.activity.LoginActivity
import app.android.snappay.activity.SignUpEnterOtpActivity
import app.android.snappay.constant.BundleConstant
import app.android.snappay.databinding.FragmentSignUpAdditionalDetailsBinding
import app.android.snappay.model.request.SignUpRequest
import app.android.snappay.model.response.SignUpResponse
import app.android.snappay.util.ErrorUtil
import app.android.snappay.util.KeyboardUtils
import app.android.snappay.util.SharedPreferenceUtil
import app.android.snappay.util.ToastUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_sign_up_additional_details.*

class SignUpAdditionalDetailsFragment : BaseFragment(), View.OnClickListener {

    private lateinit var binding: FragmentSignUpAdditionalDetailsBinding

    private val location: String
        get() = arguments?.getString(BundleConstant.LOCATION) ?: ""

    private val country_code: String
        get() = arguments?.getString(BundleConstant.COUNTRY_CODE) ?: ""

    private val mobile_number: Long
        get() = arguments?.getLong(BundleConstant.MOBILE_NUMBER) ?: 0

    private val email_id: String
        get() = arguments?.getString(BundleConstant.EMAIL_ID) ?: ""

    private val password: String
        get() = arguments?.getString(BundleConstant.PASSWORD) ?: ""

    private val first_name: String
        get() = arguments?.getString(BundleConstant.FIRST_NAME) ?: ""

    private val last_name: String
        get() = arguments?.getString(BundleConstant.LAST_NAME) ?: ""

    private var signUpDisposable: Disposable? = null

    companion object {
        @Suppress("unused")
        val TAG = SignUpAdditionalDetailsFragment::class.simpleName

        fun newInstance(
                location: String,
                country_code: String,
                mobile_number: Long,
                email_id: String,
                password: String,
                first_name: String,
                last_name: String
        ) = SignUpAdditionalDetailsFragment().apply {
            arguments = Bundle().apply {
                putString(BundleConstant.LOCATION, location)
                putString(BundleConstant.COUNTRY_CODE, country_code)
                putLong(BundleConstant.MOBILE_NUMBER, mobile_number)
                putString(BundleConstant.EMAIL_ID, email_id)
                putString(BundleConstant.PASSWORD, password)
                putString(BundleConstant.FIRST_NAME, first_name)
                putString(BundleConstant.LAST_NAME, last_name)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up_additional_details, container, false)
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
        ll_container.setOnClickListener(this)
        iv_back.setOnClickListener(this)
        tv_login.setOnClickListener(this)
        btn_sign_up.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fl_root -> KeyboardUtils.hideKeyboard(context)
            R.id.ll_container -> KeyboardUtils.hideKeyboard(context)
            R.id.iv_back -> activity?.onBackPressed()
            R.id.tv_login -> {
                startActivity(Intent(context, LoginActivity::class.java))
                activity?.finish()
            }
            R.id.btn_sign_up -> {
                KeyboardUtils.hideKeyboard(context)
                if (isValidInput()) {
                    updateUi(false)
                    signUpDisposable = (activity as BaseActivity).apiService.signUp(
                            SignUpRequest(
                                    location = location,
                                    country_code = country_code,
                                    mobile_number = mobile_number,
                                    email_id = email_id,
                                    password = password,
                                    first_name = first_name,
                                    last_name = last_name,

                                    bank_name = et_bank_name.text.toString(),
                                    account_name = et_account_name.text.toString(),
                                    iban_code = et_iban.text.toString(),
                                    swift_code = et_swift_code.text.toString(),
                                    routing_number = et_routing_number.text.toString(),
                                    question1_id = (spinner_security_question_one.selectedItemPosition + 1).toString(),
                                    question2_id = (spinner_security_question_two.selectedItemPosition + 1).toString(),
                                    question1_answer = et_answer_one.text.toString(),
                                    question2_answer = et_answer_two.text.toString()
                            )
                    ).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    { onSignUpSuccess(it) },
                                    { onSignUpError(it) }
                            )
                }
            }
        }
    }

    private fun updateUi(isEnable: Boolean) {
        main_progress_bar.visibility = if (isEnable) View.GONE else View.VISIBLE
        iv_back.isEnabled = isEnable
        spinner_security_question_one.isEnabled = isEnable
        et_answer_one.isEnabled = isEnable
        spinner_security_question_two.isEnabled = isEnable
        et_answer_two.isEnabled = isEnable
        et_bank_name.isEnabled = isEnable
        et_account_name.isEnabled = isEnable
        et_iban.isEnabled = isEnable
        et_swift_code.isEnabled = isEnable
        et_routing_number.isEnabled = isEnable
        tv_login.isEnabled = isEnable
        btn_sign_up.isEnabled = isEnable
    }

    private fun onSignUpSuccess(signUpResponse: SignUpResponse) {
        updateUi(true)
        if (isVisible) {
            SharedPreferenceUtil.getInstance(context!!).saveLoginResponse(signUpResponse.response)
            startActivity(Intent(context, SignUpEnterOtpActivity::class.java))
            activity?.finish()
        }
    }

    private fun onSignUpError(throwable: Throwable) {
        updateUi(true)
        ErrorUtil.handlerGeneralError(context, fl_root, throwable)
    }

    fun isValidInput(): Boolean {
        if (spinner_security_question_one.selectedItemPosition == spinner_security_question_two.selectedItemPosition) {
            ToastUtils.show(context, getString(R.string.message_same_security_ques))
            return false
        }

        if (et_answer_one.text.isEmpty()) {
            et_answer_one.requestFocus()
            ToastUtils.show(context, getString(R.string.error_empty_answer))
            return false
        }

        if (et_answer_two.text.isEmpty()) {
            et_answer_two.requestFocus()
            ToastUtils.show(context, getString(R.string.error_empty_answer))
            return false
        }

        if (et_bank_name.text.isEmpty()) {
            et_bank_name.requestFocus()
            ToastUtils.show(context, getString(R.string.error_empty_bank_name))
            return false
        }

        if (et_account_name.text.isEmpty()) {
            et_account_name.requestFocus()
            ToastUtils.show(context, getString(R.string.error_empty_account_name))
            return false
        }

        if (et_iban.text.isEmpty()) {
            et_iban.requestFocus()
            ToastUtils.show(context, getString(R.string.error_empty_iban))
            return false
        }

        if (et_swift_code.text.isEmpty()) {
            et_swift_code.requestFocus()
            ToastUtils.show(context, getString(R.string.error_empty_swift_code))
            return false
        }

        if (et_routing_number.text.isEmpty()) {
            et_routing_number.requestFocus()
            ToastUtils.show(context, getString(R.string.error_empty_routing_number))
            return false
        }

        return true
    }

    override fun onDestroy() {
        signUpDisposable?.dispose()
        super.onDestroy()
    }

}
