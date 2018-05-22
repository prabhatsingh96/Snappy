package app.android.snappay.activity

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import app.android.snappay.R
import app.android.snappay.constant.Constant.Companion.DELAY_FOR_NEXT_SCREEN
import app.android.snappay.databinding.ActivitySplashBinding
import app.android.snappay.util.SharedPreferenceUtil
import app.android.snappay.util.toBoolean

class SplashActivity : BaseActivity() {

    private lateinit var binding: ActivitySplashBinding

    companion object {
        var TAG = SplashActivity::class.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        init(savedInstanceState)
        initControl()
    }

    override fun init(savedInstanceState: Bundle?) {
        startNextScreen()
    }

    override fun initControl() {
    }

    private fun startNextScreen() {
        Handler().postDelayed({
            if (SharedPreferenceUtil.getInstance(this@SplashActivity).isLoggedIn) {
                if (!SharedPreferenceUtil.getInstance(this).is_varified.toBoolean) {
                    startActivity(Intent(this, SignUpEnterOtpActivity::class.java))
                    return@postDelayed
                }

                if (!SharedPreferenceUtil.getInstance(this).is_pin_set.toBoolean) {
                    startActivity(Intent(this, SetPinActivity::class.java))
                    return@postDelayed
                }
                startActivity(Intent(this, EnterPinActivity::class.java))
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }

        }, DELAY_FOR_NEXT_SCREEN)
    }
}
