package app.android.snappay.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import app.android.snappay.R
import app.android.snappay.R.id.*
import app.android.snappay.databinding.ActivityShowQrCodeBinding
import app.android.snappay.util.ImageUtils
import app.android.snappay.util.SharedPreferenceUtil
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.android.synthetic.main.activity_show_qr_code.*

class ShowQrCodeActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityShowQrCodeBinding

    companion object {
        var TAG = ShowQrCodeActivity::class.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_show_qr_code)
        init(savedInstanceState)
        initControl()
    }

    override fun init(savedInstanceState: Bundle?) {
        setSupportActionBar(toolbar)

        tv_toolbar_title.text = SharedPreferenceUtil.getInstance(this).fullName

        ImageUtils.setImage(
                imageView = iv_profile,
                isCircleCrop = true,
                load = R.drawable.ic_profile_placeholder,
                placeholderResId = R.drawable.ic_profile_placeholder,
                errorResId = R.drawable.ic_profile_placeholder
        )

        QRCodeWriter().apply {
            iv_qr_code.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    iv_qr_code.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    encode(SharedPreferenceUtil.getInstance(this@ShowQrCodeActivity).id, BarcodeFormat.QR_CODE, iv_qr_code.width, iv_qr_code.height).apply {
                        BarcodeEncoder().createBitmap(this).apply {
                            iv_qr_code.setImageBitmap(this)
                        }
                    }
                }
            })
        }
    }

    override fun initControl() {
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onClick(v: View) {
        when (v.id) {
        }
    }

}