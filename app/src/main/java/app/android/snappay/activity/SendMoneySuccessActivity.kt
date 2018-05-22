package app.android.snappay.activity

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import app.android.snappay.R
import app.android.snappay.constant.BundleConstant
import app.android.snappay.databinding.ActivitySendMoneySuccessBinding
import app.android.snappay.model.bean.CurrencyBean
import app.android.snappay.model.bean.UserBean
import app.android.snappay.util.KeyboardUtils
import app.android.snappay.util.SpannableStringUtils
import kotlinx.android.synthetic.main.activity_send_money_success.*

class SendMoneySuccessActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySendMoneySuccessBinding

    private val userBean: UserBean
        get() = intent.getParcelableExtra(BundleConstant.USER_BEAN)

    private val amount: Double
        get() = intent.getDoubleExtra(BundleConstant.AMOUNT, 0.0)

    private val currencyBean: CurrencyBean
        get() = intent.getParcelableExtra(BundleConstant.CURRENCY_BEAN)

    companion object {
        var TAG = SendMoneySuccessActivity::class.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_send_money_success)
        init(savedInstanceState)
        initControl()
    }

    override fun init(savedInstanceState: Bundle?) {
        tv_success_msg.text = SpannableStringUtils.getSpannableString(
                getString(R.string.you_have_successfully_send_s_s_to_s),
                currencyBean.currencyCode,
                amount.toString(),
                userBean.fullName
        )
    }

    override fun initControl() {
        fl_root.setOnClickListener(this)
        btn_done.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fl_root -> KeyboardUtils.hideKeyboard(this)
            R.id.btn_done -> {
                lauchHome()
            }
        }
    }

    override fun onBackPressed() {
        lauchHome()
    }

    private fun lauchHome() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
    }
}