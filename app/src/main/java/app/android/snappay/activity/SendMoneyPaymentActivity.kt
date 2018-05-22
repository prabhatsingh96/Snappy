package app.android.snappay.activity

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import app.android.snappay.R
import app.android.snappay.constant.BundleConstant
import app.android.snappay.databinding.ActivitySendMoneyPaymentActivityBinding
import app.android.snappay.model.bean.CurrencyBean
import app.android.snappay.model.bean.UserBean
import app.android.snappay.model.request.SummaryRequest
import app.android.snappay.model.response.SummaryResponse
import app.android.snappay.model.response.WalletBean
import app.android.snappay.util.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_send_money_payment_activity.*
import kotlin.math.max
import kotlin.math.min

class SendMoneyPaymentActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySendMoneyPaymentActivityBinding
    private var getSummaryDisposable: Disposable? = null

    private val userBean: UserBean
        get() = intent.getParcelableExtra(BundleConstant.USER_BEAN)

    private val amount: Double
        get() = intent.getDoubleExtra(BundleConstant.AMOUNT, 0.0)

    private val currencyBean: CurrencyBean
        get() = intent.getParcelableExtra(BundleConstant.CURRENCY_BEAN)

    private val walletBean: WalletBean
        get() = intent.getParcelableExtra(BundleConstant.WALLET_BEAN)

    private var cardId: String? = null

    companion object {
        var TAG = SendMoneyPaymentActivity::class.simpleName
        const val RC_ADD_CARD = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_send_money_payment_activity)
        init(savedInstanceState)
        initControl()
    }

    override fun init(savedInstanceState: Bundle?) {
        setSupportActionBar(toolbar)
        tv_toolbar_title.text = String.format(getString(R.string.send_s), userBean.first_name)
        tv_total_amount.text = SpannableStringUtils.getSpannableString(getString(R.string.s_s), currencyBean.currencyCode, amount.toString())
        tv_available_balance_sub_label.text = SpannableStringUtils.getSpannableString(getString(R.string.available_balance_s_s), currencyBean.currencyCode, walletBean.left_amount.toString())
        displayToBeDeductedAmount()
    }

    private fun displayToBeDeductedAmount() {
        if (chk_send_with_wallet.isChecked) {
            tv_send_with_wallet_amount.text = SpannableStringUtils.getSpannableString(getString(R.string.s_s), currencyBean.currencyCode, min(walletBean.left_amount, amount).toString())
            tv_remaining_bal.text = SpannableStringUtils.getSpannableString(getString(R.string.s_s), currencyBean.currencyCode, max(0.0, amount - walletBean.left_amount).toString())
        } else {
            tv_send_with_wallet_amount.text = SpannableStringUtils.getSpannableString(getString(R.string.s_s), currencyBean.currencyCode, "0")
            tv_remaining_bal.text = SpannableStringUtils.getSpannableString(getString(R.string.s_s), currencyBean.currencyCode, max(0.0, amount).toString())
        }
    }

    override fun initControl() {
        fl_root.setOnClickListener(this)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        chk_send_with_wallet.setOnCheckedChangeListener { buttonView, isChecked -> displayToBeDeductedAmount() }
        tv_add_new_card.setOnClickListener(this)
        btn_proceed_to_pay.setOnClickListener(this)
    }

    private fun updateUi(isEnable: Boolean) {
        main_progress_bar.visibility = if (isEnable) View.GONE else View.VISIBLE
        chk_send_with_wallet.isEnabled = isEnable
        tv_cards.isEnabled = isEnable
        tv_banks.isEnabled = isEnable
        tv_add_new_card.isEnabled = isEnable
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fl_root -> KeyboardUtils.hideKeyboard(this)
            R.id.tv_add_new_card -> {
                startActivityForResult(Intent(this, AddNewCardActivity::class.java), RC_ADD_CARD)
            }
            R.id.btn_proceed_to_pay -> {
                KeyboardUtils.hideKeyboard(this)
                if (isValidInput()) {
                    updateUi(false)
                    getSummaryDisposable = apiService.getSummary(
                            access_token = SharedPreferenceUtil.getInstance(this).access_token,
                            summaryRequest = SummaryRequest(
                                    receiver_user_id = userBean.id,
                                    amount = amount,
                                    currency_type = currencyBean.currencyCode,
                                    payWithWallet = chk_send_with_wallet.isChecked.toInt,
                                    card_id = cardId
                            )
                    ).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    { onFetchSummarySuccess(it) },
                                    { onFetchSummaryError(it) }
                            )

                }
            }
        }
    }

    private fun onFetchSummarySuccess(summaryResponse: SummaryResponse) {
        updateUi(true)
        startActivity(Intent(this, SendMoneySummaryActivity::class.java).apply {
            putExtra(BundleConstant.AMOUNT, amount)
            putExtra(BundleConstant.USER_BEAN, userBean)
            putExtra(BundleConstant.SUMMARY_BEAN, summaryResponse.response)
            putExtra(BundleConstant.CURRENCY_BEAN, currencyBean)
            putExtra(BundleConstant.CARD_ID, cardId)
            putExtra(BundleConstant.PAY_WITH_WALLET, chk_send_with_wallet.isChecked)
        })
    }

    private fun onFetchSummaryError(throwable: Throwable) {
        updateUi(true)
        ErrorUtil.handlerGeneralError(this, fl_root, throwable)
    }

    fun isValidInput(): Boolean {
        when (chk_send_with_wallet.isChecked) {
            true -> {
                if (walletBean.left_amount < amount && cardId == null) {
                    ToastUtils.show(this, getString(R.string.error_select_option_to_send_remaining_balance))
                    return false
                }
            }
            false -> {
                if (cardId == null) {
                    ToastUtils.show(this, getString(R.string.error_select_payment_method_to_send_money))
                    return false
                }
            }
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RC_ADD_CARD -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        data?.let {
                            cardId = it.getStringExtra(BundleConstant.CARD_ID)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        getSummaryDisposable?.dispose()
        super.onDestroy()
    }

}