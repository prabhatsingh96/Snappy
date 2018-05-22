package app.android.snappay.fragment

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.android.snappay.R
import app.android.snappay.activity.BaseActivity
import app.android.snappay.activity.HomeActivity
import app.android.snappay.activity.SetPinActivity
import app.android.snappay.databinding.FragmentConfirmPinBinding
import app.android.snappay.model.request.SetPinRequest
import app.android.snappay.model.response.SetPinResponse
import app.android.snappay.util.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_confirm_pin.*

class ConfirmPinFragment : BaseFragment(), View.OnClickListener {

    private lateinit var binding: FragmentConfirmPinBinding
    private var setPinDisposable: Disposable? = null

    companion object {
        @Suppress("unused")
        val TAG = ConfirmPinFragment::class.simpleName

        fun newInstance() = ConfirmPinFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_confirm_pin, container, false)
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
                    setPinDisposable = (activity as BaseActivity).apiService.setPin(
                            access_token = SharedPreferenceUtil.getInstance(context!!).access_token,
                            setPinRequest = SetPinRequest(et_pin.text.toString())
                    ).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    { onSetPinSuccess(it) },
                                    { onSetPinFailure(it) }
                            )
                }
            }
        }
    }

    private fun onSetPinSuccess(setPinResponse: SetPinResponse) {
        if (isVisible) {
            updateUi(true)
            if (setPinResponse.response.is_pin_set.toBoolean) {
                SharedPreferenceUtil.getInstance(context!!).saveLoginResponse(setPinResponse.response)
                SharedPreferenceUtil.getInstance(context!!).isLoggedIn = true
                val intent = Intent(context, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                activity?.finish()
            }
        }
    }

    private fun onSetPinFailure(throwable: Throwable) {
        if (isVisible) {
            updateUi(true)
            ErrorUtil.handlerGeneralError(context, fl_root, throwable)
        }
    }

    private fun updateUi(isEnable: Boolean) {
        main_progress_bar.visibility = if (isEnable) View.GONE else View.VISIBLE
        iv_back.isEnabled = isEnable
        et_pin.isEnabled = isEnable
        btn_next.isEnabled = isEnable
    }

    fun isValidInput(): Boolean {
        if (et_pin.text.isEmpty()) {
            et_pin.requestFocus()
            ToastUtils.show(context, getString(R.string.error_empty_pin))
            return false
        }
        if (et_pin.text.count() != resources.getInteger(R.integer.default_pin_length)) {
            et_pin.requestFocus()
            ToastUtils.show(context, String.format(getString(R.string.error_invalid_pin_length), resources.getInteger(R.integer.default_pin_length)))
            return false
        }

        if (!(activity as SetPinActivity).binding.etPin.text.toString().equals(et_pin.text.toString())) {
            et_pin.requestFocus()
            ToastUtils.show(context, getString(R.string.error_diff_confirm_pin))
            return false
        }
        return true
    }

    override fun onDestroy() {
        setPinDisposable?.dispose()
        super.onDestroy()
    }
}
