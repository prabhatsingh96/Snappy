package app.android.snappay.activity

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import app.android.snappay.R
import app.android.snappay.constant.BundleConstant
import app.android.snappay.databinding.ActivityAddNewCardBinding
import app.android.snappay.model.request.AddCardRequest
import app.android.snappay.model.response.AddCardResponse
import app.android.snappay.util.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_new_card.*

class AddNewCardActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAddNewCardBinding
    private var addCardDisposable: Disposable? = null

    companion object {
        var TAG = AddNewCardActivity::class.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_new_card)
        init(savedInstanceState)
        initControl()
    }

    override fun init(savedInstanceState: Bundle?) {
        setSupportActionBar(toolbar)
    }

    override fun initControl() {
        fl_root.setOnClickListener(this)
        ll_container.setOnClickListener(this)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        btn_add_card.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fl_root -> KeyboardUtils.hideKeyboard(this)
            R.id.ll_container -> KeyboardUtils.hideKeyboard(this)
            R.id.btn_add_card -> {
                KeyboardUtils.hideKeyboard(this)
                if (isValidInput()) {
                    updateUi(false)
                    addCardDisposable = apiService.addCard(
                            access_token = SharedPreferenceUtil.getInstance(this).access_token,
                            addCardRequest = AddCardRequest(
                                    card_name = et_card_name.text.toString(),
                                    card_number = et_card_number.text.toString().toLong(),
                                    cvv = et_cvv.text.toString().toInt(),
                                    mm = spinner_month.selectedItemPosition,
                                    yy = resources.getStringArray(R.array.years)[spinner_year.selectedItemPosition],
                                    prefrence = check_save_card.isChecked.toInt
                            )
                    ).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    { onAddCardSuccess(it) },
                                    { onAddCardError(it) }
                            )
                }
            }
        }
    }

    private fun updateUi(isEnable: Boolean) {
        main_progress_bar.visibility = if (isEnable) View.GONE else View.VISIBLE
        et_card_name.isEnabled = isEnable
        et_card_number.isEnabled = isEnable
        spinner_month.isEnabled = isEnable
        spinner_year.isEnabled = isEnable
        et_cvv.isEnabled = isEnable
        check_save_card.isEnabled = isEnable
        btn_add_card.isEnabled = isEnable
    }

    private fun onAddCardSuccess(addCardResponse: AddCardResponse) {
        updateUi(true)
        ToastUtils.show(this, getString(R.string.message_card_added))
        Intent().apply {
            putExtra(BundleConstant.CARD_ID, addCardResponse.response.card_id)
            setResult(Activity.RESULT_OK, this)
            finish()
        }
    }

    private fun onAddCardError(throwable: Throwable) {
        updateUi(true)
        ErrorUtil.handlerGeneralError(this, fl_root, throwable)
    }

    fun isValidInput(): Boolean {

        if (et_card_name.text.isEmpty()) {
            et_card_name.requestFocus()
            ToastUtils.show(this, getString(R.string.error_invalid_card_name))
            return false
        }

        if (et_card_number.text.isEmpty()) {
            et_card_number.requestFocus()
            ToastUtils.show(this, getString(R.string.error_invalid_card_number))
            return false
        }

        if (spinner_month.selectedItemPosition == 0) {
            spinner_month.requestFocus()
            ToastUtils.show(this, getString(R.string.error_invalid_month))
            return false
        }

        if (et_cvv.text.isEmpty()) {
            et_cvv.requestFocus()
            ToastUtils.show(this, getString(R.string.error_invalid_cvv))
            return false
        }

        return true
    }

    override fun onDestroy() {
        addCardDisposable?.dispose()
        super.onDestroy()
    }
}