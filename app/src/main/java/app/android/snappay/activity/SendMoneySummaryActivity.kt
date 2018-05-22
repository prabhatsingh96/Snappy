package app.android.snappay.activity

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import app.android.snappay.R
import app.android.snappay.constant.BundleConstant
import app.android.snappay.databinding.ActivitySendMoneySummaryBinding
import app.android.snappay.model.bean.CurrencyBean
import app.android.snappay.model.bean.UserBean
import app.android.snappay.model.request.SendMoneyRequest
import app.android.snappay.model.response.SendMoneyResponse
import app.android.snappay.model.response.SummaryBean
import app.android.snappay.util.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_send_money_summary.*

class SendMoneySummaryActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySendMoneySummaryBinding
    private var note: String? = null

    private var sendMoneyDisposable: Disposable? = null

    private val userBean: UserBean
        get() = intent.getParcelableExtra(BundleConstant.USER_BEAN)

    private val amount: Double
        get() = intent.getDoubleExtra(BundleConstant.AMOUNT, 0.0)

    private val currencyBean: CurrencyBean
        get() = intent.getParcelableExtra(BundleConstant.CURRENCY_BEAN)

    private val summaryBean: SummaryBean
        get() = intent.getParcelableExtra(BundleConstant.SUMMARY_BEAN)

    private val cardId: String?
        get() = intent.getStringExtra(BundleConstant.CARD_ID)

    private val payWithWallet: Boolean
        get() = intent.getBooleanExtra(BundleConstant.PAY_WITH_WALLET, true)

    companion object {
        var TAG = SendMoneySummaryActivity::class.simpleName
        const val RC_ADD_NOTE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_send_money_summary)
        init(savedInstanceState)
        initControl()
    }

    override fun init(savedInstanceState: Bundle?) {
        setSupportActionBar(toolbar)
        tv_send_s_s_to.text = SpannableStringUtils.getSpannableString(getString(R.string.send_s_s_to), currencyBean.currencyCode, amount.toString())

        ImageUtils.setImage(
                imageView = iv_profile,
                isCircleCrop = true,
                load = R.drawable.ic_profile_placeholder,
                placeholderResId = R.drawable.ic_profile_placeholder,
                errorResId = R.drawable.ic_profile_placeholder
        )

        tv_full_name.text = userBean.fullName
        tv_email.text = userBean.email_id

        fund_send_from_card_container.visibility = if (summaryBean.card_received_amount == 0.0) View.GONE else View.VISIBLE
        tv_amount_deducted_from_card.text = SpannableStringUtils.getSpannableString(getString(R.string.s_s), currencyBean.currencyCode, summaryBean.card_received_amount.toString())
        tv_card_no.text = summaryBean.formatted_card_number

        fund_send_from_wallet_container.visibility = if (summaryBean.wallet_deduct_amount == 0.0) View.GONE else View.VISIBLE
        tv_amount_deducted_from_wallet.text = SpannableStringUtils.getSpannableString(getString(R.string.s_s), currencyBean.currencyCode, summaryBean.wallet_deduct_amount.toString())

        tv_service_tax.text = SpannableStringUtils.getSpannableString(getString(R.string.s_s), currencyBean.currencyCode, summaryBean.service_tax.toString())
        tv_sgst.text = SpannableStringUtils.getSpannableString(getString(R.string.s_s), currencyBean.currencyCode, summaryBean.gst.toString())

        tv_total_amount.text = SpannableStringUtils.getSpannableString(getString(R.string.s_s), currencyBean.currencyCode, summaryBean.total_amount.toString())
    }

    override fun initControl() {
        fl_root.setOnClickListener(this)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        tv_add_note.setOnClickListener(this)
        btn_proceed_to_send.setOnClickListener(this)
    }

    private fun updateUi(isEnable: Boolean) {
        main_progress_bar.visibility = if (isEnable) View.GONE else View.VISIBLE
        tv_add_note.isEnabled = isEnable
        btn_proceed_to_send.isEnabled = isEnable
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fl_root -> KeyboardUtils.hideKeyboard(this)
            R.id.tv_add_note -> startActivityForResult(Intent(this, AddNoteActivity::class.java).apply {
                putExtra(BundleConstant.NOTE, note ?: "")
            }, RC_ADD_NOTE)
            R.id.btn_proceed_to_send -> {
                updateUi(false)
                sendMoneyDisposable = apiService.sendMoney(
                        access_token = SharedPreferenceUtil.getInstance(this).access_token,
                        sendMoneyRequest = SendMoneyRequest(
                                receiver_user_id = userBean.id,
                                amount = amount,
                                currency_type = currencyBean.currencyCode,
                                payWithWallet = payWithWallet.toInt,
                                card_id = cardId,
                                note = note
                        )
                ).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { onSendMoneySuccess(it) },
                                { onSendMoneyFailure(it) }
                        )

            }
        }
    }

    private fun onSendMoneySuccess(sendMoneyResponse: SendMoneyResponse) {
        updateUi(true)
        startActivity(Intent(this, SendMoneySuccessActivity::class.java).apply {
            putExtra(BundleConstant.AMOUNT, amount)
            putExtra(BundleConstant.USER_BEAN, userBean)
            putExtra(BundleConstant.CURRENCY_BEAN, currencyBean)
        })
    }

    private fun onSendMoneyFailure(throwable: Throwable) {
        updateUi(true)
        ErrorUtil.handlerGeneralError(this, fl_root, throwable)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RC_ADD_NOTE -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        data?.let {
                            note = it.getStringExtra(BundleConstant.NOTE)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        sendMoneyDisposable?.dispose()
        super.onDestroy()
    }
}
