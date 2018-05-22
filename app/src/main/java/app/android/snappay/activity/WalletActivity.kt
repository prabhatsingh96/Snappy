package app.android.snappay.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import app.android.snappay.R
import app.android.snappay.databinding.ActivityWalletBinding
import app.android.snappay.model.response.WalletDetailResponse
import app.android.snappay.util.ErrorUtil
import app.android.snappay.util.SharedPreferenceUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_wallet.*
import java.util.*

class WalletActivity : BaseActivity(), View.OnClickListener {

    private var fetchWalletDetailsDisposable: Disposable? = null

    companion object {
        var TAG = WalletActivity::class.simpleName
    }

    private lateinit var binding: ActivityWalletBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wallet)
        init(savedInstanceState)
        initControl()
    }

    override fun init(savedInstanceState: Bundle?) {
        setSupportActionBar(toolbar)
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

    override fun initControl() {
        toolbar.setNavigationOnClickListener { onBackPressed() }
        btn_add_money.setOnClickListener(this)
    }

    private fun updateUi(isEnable: Boolean) {
        main_progress_bar.visibility = if (isEnable) View.GONE else View.VISIBLE
        btn_add_money.isEnabled = isEnable
    }

    private fun onFetchWalletDetailsSuccess(walletDetailResponse: WalletDetailResponse) {
        updateUi(true)
        tv_available_balance.text = String.format(Locale.getDefault(), getString(R.string.s_s, walletDetailResponse.response.currency_Symbol
                ?: walletDetailResponse.response.currency_type, walletDetailResponse.response.left_amount.toString()))
    }

    private fun onFetchWalletDetailsError(throwable: Throwable) {
        updateUi(true)
        ErrorUtil.handlerGeneralError(this, fl_root, throwable)
    }

    override fun onClick(v: View) {
        when (v.id) {
        }
    }

    override fun onDestroy() {
        fetchWalletDetailsDisposable?.dispose()
        super.onDestroy()
    }

}