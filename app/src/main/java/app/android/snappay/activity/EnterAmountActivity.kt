package app.android.snappay.activity

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import android.view.inputmethod.BaseInputConnection
import app.android.snappay.R
import app.android.snappay.constant.BundleConstant
import app.android.snappay.databinding.ActivityEnterAmountBinding
import app.android.snappay.model.bean.CurrencyBean
import app.android.snappay.model.bean.UserBean
import app.android.snappay.model.response.WalletDetailResponse
import app.android.snappay.util.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_enter_amount.*

class EnterAmountActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityEnterAmountBinding
    private lateinit var etAmountInputConnection: BaseInputConnection

    private lateinit var currencyBean: CurrencyBean

    private val userBean: UserBean
        get() = intent.getParcelableExtra(BundleConstant.USER_BEAN)

    private var fetchWalletDetailsDisposable: Disposable? = null

    companion object {
        var TAG = SendMoneyActivity::class.simpleName
        const val RC_CHANGE_CURRENCY = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_enter_amount)
        init(savedInstanceState)
        initControl()
    }

    override fun init(savedInstanceState: Bundle?) {
        setSupportActionBar(toolbar)
        tv_toolbar_title.text = String.format(getString(R.string.send_s), userBean.first_name)
        etAmountInputConnection = BaseInputConnection(et_amount, true)
        /*for AE we have the currency */
        currencyBean = CurrencyUtils.getCurrency(getString(R.string.default_country_code))!!
        initCurrency()
    }

    private fun initCurrency() {
        et_amount.setText(currencyBean.currencySymbol)
        tv_currency.text = String.format(getString(R.string.s_next), currencyBean.currencyCode)
    }

    override fun initControl() {
        fl_root.setOnClickListener(this)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        tv_currency.setOnClickListener(this)
        key_1.setOnClickListener(this)
        key_2.setOnClickListener(this)
        key_3.setOnClickListener(this)
        key_4.setOnClickListener(this)
        key_5.setOnClickListener(this)
        key_6.setOnClickListener(this)
        key_7.setOnClickListener(this)
        key_8.setOnClickListener(this)
        key_9.setOnClickListener(this)
        key_00.setOnClickListener(this)
        key_0.setOnClickListener(this)
        key_back.setOnClickListener(this)
        btn_proceed_to_pay.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fl_root -> KeyboardUtils.hideKeyboard(this)

            R.id.tv_currency -> {
                startActivityForResult(Intent(this, SelectCurrencyActivity::class.java), RC_CHANGE_CURRENCY)
            }

            R.id.key_1 -> {
                et_amount.append("1")
            }
            R.id.key_2 -> {
                et_amount.append("2")
            }
            R.id.key_3 -> {
                et_amount.append("3")
            }
            R.id.key_4 -> {
                et_amount.append("4")
            }
            R.id.key_5 -> {
                et_amount.append("5")
            }
            R.id.key_6 -> {
                et_amount.append("6")
            }
            R.id.key_7 -> {
                et_amount.append("7")
            }
            R.id.key_8 -> {
                et_amount.append("8")
            }
            R.id.key_9 -> {
                et_amount.append("9")
            }
            R.id.key_00 -> {
                et_amount.append("00")
            }
            R.id.key_0 -> {
                et_amount.append("0")
            }
            R.id.key_back -> {
                if (et_amount.text.length > currencyBean.currencySymbol.length) {
                    et_amount.setText(et_amount.text.substring(0..(et_amount.text.length - 2)))
                }
            }
            R.id.btn_proceed_to_pay -> {
                if (isValidInput()) {
                    updateUi(false)
                    fetchWalletDetailsDisposable = apiService.getWalletDetails(
                            access_token = SharedPreferenceUtil.getInstance(this).access_token
                    ).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    { onFetchWalletDetailsSuccess(it) },
                                    { onFetchWalletDetailsError(it) }
                            )
                }
            }
        }
    }

    fun isValidInput(): Boolean {
        if (et_amount.text.length <= currencyBean.currencySymbol.length) {
            et_amount.requestFocus()
            ToastUtils.show(this, getString(R.string.error_invalid_amount))
            return false
        }
        return true
    }

    private fun updateUi(isEnable: Boolean) {
        main_progress_bar.visibility = if (isEnable) View.GONE else View.VISIBLE
        tv_currency.isEnabled = isEnable
        key_1.isEnabled = isEnable
        key_2.isEnabled = isEnable
        key_3.isEnabled = isEnable
        key_4.isEnabled = isEnable
        key_5.isEnabled = isEnable
        key_6.isEnabled = isEnable
        key_7.isEnabled = isEnable
        key_8.isEnabled = isEnable
        key_9.isEnabled = isEnable
        key_00.isEnabled = isEnable
        key_0.isEnabled = isEnable
        key_back.isEnabled = isEnable
        btn_proceed_to_pay.isEnabled = isEnable
    }

    private fun onFetchWalletDetailsSuccess(walletDetailResponse: WalletDetailResponse) {
        updateUi(true)
        startActivity(
                Intent(this, SendMoneyPaymentActivity::class.java).apply {
                    putExtra(BundleConstant.USER_BEAN, userBean)
                    putExtra(BundleConstant.WALLET_BEAN, walletDetailResponse.response)
                    putExtra(BundleConstant.AMOUNT, et_amount.text.toString().replace(currencyBean.currencySymbol, "").toDouble())
                    /*User selected currency or default currency...*/
                    putExtra(BundleConstant.CURRENCY_BEAN, currencyBean)
                }
        )
    }

    private fun onFetchWalletDetailsError(throwable: Throwable) {
        updateUi(true)
        ErrorUtil.handlerGeneralError(this, fl_root, throwable)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RC_CHANGE_CURRENCY -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        data?.let {
                            //                            currencyBean = it.getParcelableExtra<CurrencyBean>(BundleConstant.CURRENCY_BEAN)
//                            initCurrency()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        fetchWalletDetailsDisposable?.dispose()
        super.onDestroy()
    }
}
