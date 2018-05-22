package app.android.snappay.activity

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import app.android.snappay.R
import app.android.snappay.databinding.ActivitySnapPayBinding
import com.journeyapps.barcodescanner.CaptureManager
import kotlinx.android.synthetic.main.activity_snap_pay.*

class SnapPayActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivitySnapPayBinding

    companion object {
        var TAG = SnapPayActivity::class.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_snap_pay)
        init(savedInstanceState)
        initControl()
    }

    private lateinit var captureManager: CaptureManager

    override fun init(savedInstanceState: Bundle?) {
        setSupportActionBar(toolbar)
        captureManager = CaptureManager(this, barcode_scanner)
        captureManager.initializeFromIntent(intent, savedInstanceState)
        captureManager.decode()
    }

    override fun initControl() {
        toolbar.setNavigationOnClickListener { onBackPressed() }
        tv_show_qr_code.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_show_qr_code -> {
                startActivity(Intent(this, ShowQrCodeActivity::class.java))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        captureManager.onResume()
    }

    override fun onPause() {
        super.onPause()
        captureManager.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        captureManager.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        captureManager.onSaveInstanceState(outState)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return barcode_scanner.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event)
    }

}