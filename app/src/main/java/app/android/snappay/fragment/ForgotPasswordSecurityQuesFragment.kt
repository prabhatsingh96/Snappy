package app.android.snappay.fragment

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.android.snappay.R
import app.android.snappay.activity.BaseActivity
import app.android.snappay.constant.BundleConstant
import app.android.snappay.databinding.FragmentForgotPasswordSecurityQuesBinding
import app.android.snappay.model.bean.SecurityQuestionBean
import app.android.snappay.model.request.MatchSecurityQuestionRequest
import app.android.snappay.model.response.MatchSecurityQuestionResponse
import app.android.snappay.util.ErrorUtil
import app.android.snappay.util.KeyboardUtils
import app.android.snappay.util.ToastUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_forgot_password_security_ques.*

class ForgotPasswordSecurityQuesFragment : BaseFragment(), View.OnClickListener {

    private lateinit var binding: FragmentForgotPasswordSecurityQuesBinding
    private var matchSecurityQuesDisposable: Disposable? = null

    private val mobile_number: Long?
        get() = arguments?.getLong(BundleConstant.MOBILE_NUMBER)

    private val email_id: String?
        get() = arguments?.getString(BundleConstant.EMAIL_ID)

    private val securityQuestionBean: SecurityQuestionBean
        get() = arguments!!.getParcelable(BundleConstant.SECURITY_QUESTION_BEAN)

    companion object {
        @Suppress("unused")
        val TAG = ForgotPasswordSecurityQuesFragment::class.simpleName

        fun newInstance(
                mobile_number: Long?,
                email_id: String?,
                securityQuestionBean: SecurityQuestionBean
        ) = ForgotPasswordSecurityQuesFragment().apply {
            arguments = Bundle().apply {
                mobile_number?.let { putLong(BundleConstant.MOBILE_NUMBER, it) }
                putString(BundleConstant.EMAIL_ID, email_id)
                putParcelable(BundleConstant.SECURITY_QUESTION_BEAN, securityQuestionBean)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_forgot_password_security_ques, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
        initControl()
    }

    override fun init() {
        et_answer_one.hint = resources.getStringArray(R.array.security_question)[securityQuestionBean.question1_id - 1]
        et_answer_two.hint = resources.getStringArray(R.array.security_question)[securityQuestionBean.question2_id - 1]
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
                KeyboardUtils.hideKeyboard(context)
                if (isValidInput()) {

                    updateUi(false)
                    matchSecurityQuesDisposable = (activity as BaseActivity).apiService.matchSecurityQues(
                            MatchSecurityQuestionRequest(
                                    mobile_number = mobile_number,
                                    email_id = email_id,
                                    question1_answer = et_answer_one.text.toString(),
                                    question2_answer = et_answer_two.text.toString()
                            )
                    ).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    { onMatchSecurityQuesSuccess(it) },
                                    { onMatchSecurityQuesError(it) }
                            )
                }
            }
        }
    }

    private fun updateUi(isEnable: Boolean) {
        main_progress_bar.visibility = if (isEnable) View.GONE else View.VISIBLE
        iv_back.isEnabled = isEnable
        et_answer_one.isEnabled = isEnable
        et_answer_two.isEnabled = isEnable
        btn_next.isEnabled = isEnable
    }

    private fun onMatchSecurityQuesSuccess(@Suppress("UNUSED_PARAMETER") matchSecurityQuestionResponse: MatchSecurityQuestionResponse) {
        updateUi(true)
        activity?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(
                        android.R.id.content,
                        ResetPasswordFragment.newInstance(
                                mobile_number,
                                email_id
                        ),
                        ResetPasswordFragment.TAG
                )
                ?.addToBackStack(ResetPasswordFragment.TAG)
                ?.commit()
    }

    private fun onMatchSecurityQuesError(throwable: Throwable) {
        updateUi(true)
        ErrorUtil.handlerGeneralError(context, fl_root, throwable)
    }

    fun isValidInput(): Boolean {

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

        return true
    }

    override fun onStart() {
        super.onStart()
        resetUi()
    }

    private fun resetUi() {
        et_answer_one.text = null
        et_answer_two.text = null
    }

    override fun onDestroy() {
        matchSecurityQuesDisposable?.dispose()
        super.onDestroy()
    }
}