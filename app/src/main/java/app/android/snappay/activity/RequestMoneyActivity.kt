package app.android.snappay.activity

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import app.android.snappay.R
import app.android.snappay.databinding.ActivityRequestMoneyBinding
import app.android.snappay.util.KeyboardUtils
import kotlinx.android.synthetic.main.activity_request_money.*

class RequestMoneyActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityRequestMoneyBinding

    companion object {
        var TAG = RequestMoneyActivity::class.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_request_money)
        init(savedInstanceState)
        initControl()
    }

    override fun init(savedInstanceState: Bundle?) {
        setSupportActionBar(toolbar)

    }

    override fun initControl() {
        toolbar.setNavigationOnClickListener { onBackPressed() }
        tv_show_qr_code.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_show_qr_code -> {
                KeyboardUtils.hideKeyboard(this)
                startActivity(Intent(this, ShowQrCodeActivity::class.java))
            }
        }
    }

}