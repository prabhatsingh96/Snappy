package app.android.snappay.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import app.android.snappay.R
import app.android.snappay.adapter.CurrencyAdapter
import app.android.snappay.databinding.ActivitySelectCurrencyBinding
import app.android.snappay.model.bean.CurrencyBean
import app.android.snappay.util.CurrencyUtils
import app.android.snappay.util.KeyboardUtils
import com.jakewharton.rxbinding2.widget.RxTextView
import kotlinx.android.synthetic.main.activity_select_currency.*
import java.util.*

class SelectCurrencyActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySelectCurrencyBinding

    private val currencies = ArrayList<CurrencyBean>()

    companion object {
        var TAG = SelectCurrencyActivity::class.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_currency)
        init(savedInstanceState)
        initControl()
    }

    override fun init(savedInstanceState: Bundle?) {
        setSupportActionBar(toolbar)

        for (countryCode in Locale.getISOCountries()) {
            CurrencyUtils.getCurrency(countryCode)?.let {
                currencies.add(it)
            }
        }
        rv_currency.adapter = CurrencyAdapter(this, currencies)
    }

    override fun initControl() {
        fl_root.setOnClickListener(this)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        RxTextView.textChangeEvents(et_search)
                .subscribe { query ->
                    (rv_currency.adapter as CurrencyAdapter).updateDataset(
                            currencies.filter {
                                it.countryName.contains(query.text(), true) or it.currencyCode.contains(query.text(), true)
                            } as ArrayList<CurrencyBean>
                    )
                }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fl_root -> KeyboardUtils.hideKeyboard(this)
        }
    }

}