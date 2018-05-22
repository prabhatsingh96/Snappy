package app.android.snappay.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.view.View
import app.android.snappay.R
import app.android.snappay.databinding.ActivitySetPinBinding
import app.android.snappay.fragment.ConfirmPinFragment
import app.android.snappay.util.KeyboardUtils
import app.android.snappay.util.ToastUtils
import kotlinx.android.synthetic.main.activity_set_pin.*

class SetPinActivity : BaseActivity(), View.OnClickListener, FragmentManager.OnBackStackChangedListener {

    lateinit var binding: ActivitySetPinBinding

    companion object {
        var TAG = SetPinActivity::class.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_set_pin)
        init(savedInstanceState)
        initControl()
    }

    override fun init(savedInstanceState: Bundle?) {

    }

    override fun initControl() {
        ll_root.setOnClickListener(this)
        btn_next.setOnClickListener(this)

        supportFragmentManager.addOnBackStackChangedListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.ll_root -> KeyboardUtils.hideKeyboard(this)
            R.id.btn_next -> {
                KeyboardUtils.hideKeyboard(this)
                if (isValidInput()) {
                    supportFragmentManager.beginTransaction().replace(android.R.id.content, ConfirmPinFragment.newInstance(), ConfirmPinFragment.TAG).addToBackStack(ConfirmPinFragment.TAG).commit()
                }
            }
        }
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

    override fun onBackStackChanged() {
        if (supportFragmentManager.backStackEntryCount == 0) {
            resetUi()
        }
    }

    private fun resetUi() {
        et_pin.text = null
    }

}
