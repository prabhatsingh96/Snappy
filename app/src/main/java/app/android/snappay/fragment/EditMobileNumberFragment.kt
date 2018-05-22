package app.android.snappay.fragment

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.android.snappay.R
import app.android.snappay.activity.BaseActivity
import app.android.snappay.databinding.FragmentEditMobileNumberBinding
import app.android.snappay.model.request.ChangeMobileNumberRequest
import app.android.snappay.model.response.ChangeMobileNumberResponse
import app.android.snappay.util.ErrorUtil
import app.android.snappay.util.KeyboardUtils
import app.android.snappay.util.SharedPreferenceUtil
import app.android.snappay.util.ToastUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_edit_mobile_number.*

class EditMobileNumberFragment : BaseFragment(), View.OnClickListener {

    private lateinit var binding: FragmentEditMobileNumberBinding
    private var changeMobileDisposable: Disposable? = null

    companion object {
        @Suppress("unused")
        val TAG = EditMobileNumberFragment::class.simpleName

        fun newInstance() = EditMobileNumberFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_mobile_number, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
        initControl()
    }

    override fun init() {
        KeyboardUtils.hideKeyboard(context)
    }

    override fun initControl() {
        fl_root.setOnClickListener(this)
        btn_submit.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fl_root -> KeyboardUtils.hideKeyboard(context)
            R.id.btn_submit -> {
                KeyboardUtils.hideKeyboard(context)
                if (isValidInput()) {
                    updateUi(false)
                    changeMobileDisposable = (activity as BaseActivity).apiService.changeMobileNumber(
                            access_token = SharedPreferenceUtil.getInstance(context!!).access_token,
                            changeMobileNumberRequest = ChangeMobileNumberRequest(et_mobile.text.toString())
                    ).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    { onChangeMobileSuccess(it) },
                                    { onChangeMobileFailure(it) }
                            )
                }
            }
        }
    }

    private fun onChangeMobileSuccess(@Suppress("UNUSED_PARAMETER") changeMobileNumberResponse: ChangeMobileNumberResponse) {
        if (isVisible) {
            context?.let {
                updateUi(true)
                SharedPreferenceUtil.getInstance(it).mobile_number = et_mobile.text.toString().toLong()
                ToastUtils.show(context, getString(R.string.message_otp_send_to_updated_mobile))
                activity?.onBackPressed()
            }
        }
    }

    private fun onChangeMobileFailure(throwable: Throwable) {
        if (isVisible) {
            updateUi(true)
            ErrorUtil.handlerGeneralError(context, fl_root, throwable)
        }
    }

    private fun updateUi(isEnable: Boolean) {
        main_progress_bar.visibility = if (isEnable) View.GONE else View.VISIBLE
        ccp_country_code.isEnabled = isEnable
        et_mobile.isEnabled = isEnable
        btn_submit.isEnabled = isEnable
    }

    fun isValidInput(): Boolean {

        if (et_mobile.text.isEmpty()) {
            et_mobile.requestFocus()
            ToastUtils.show(context, getString(R.string.error_empty_mobile))
            return false
        }

        if (!TextUtils.isDigitsOnly(et_mobile.text)) {
            et_mobile.requestFocus()
            ToastUtils.show(context, getString(R.string.error_invalid_mobile))
            return false
        }

        return true
    }

    override fun onDestroy() {
        changeMobileDisposable?.dispose()
        super.onDestroy()
    }
}
