package app.android.snappay.activity

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import app.android.snappay.R
import app.android.snappay.constant.Constant
import app.android.snappay.databinding.ActivityHomeBinding
import app.android.snappay.fragment.EditProfileDialogFragment
import app.android.snappay.model.response.BaseResponse
import app.android.snappay.util.ErrorUtil
import app.android.snappay.util.ImageUtils
import app.android.snappay.util.SharedPreferenceUtil
import app.android.snappay.util.ToastUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.merchant.activity_home.*

class HomeActivity : BaseActivity(), View.OnClickListener {

    private var backPressedTime: Long = 0
    private var logoutDisposable: Disposable? = null
    private lateinit var binding: ActivityHomeBinding

    companion object {
        var TAG = HomeActivity::class.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        init(savedInstanceState)
        initControl()
    }

    override fun init(savedInstanceState: Bundle?) {
        setSupportActionBar(toolbar)

        val drawerToggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.drawer_open, R.string.drawer_close)
        drawer_layout.addDrawerListener(drawerToggle)

        ImageUtils.setImage(
                imageView = iv_drawer_profile,
                isCircleCrop = true,
                load = R.drawable.ic_profile_placeholder,
                placeholderResId = R.drawable.ic_profile_placeholder,
                errorResId = R.drawable.ic_profile_placeholder
        )

        ImageUtils.setImage(
                imageView = iv_edit,
                isCircleCrop = true,
                load = R.drawable.ic_edit_photo
        )

    }

    override fun initControl() {
        /*Main container*/

        /*Drawer items*/
        iv_drawer_profile.setOnClickListener(this)
        iv_edit.setOnClickListener(this)

        tv_bank.setOnClickListener(this)
        tv_login_security.setOnClickListener(this)
        tv_notification.setOnClickListener(this)
        tv_help.setOnClickListener(this)
        tv_legal_agreements.setOnClickListener(this)
        tv_logout.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            android.R.id.home -> openDrawer()
        /*Main container*/

        /*Drawer items*/
            R.id.iv_drawer_profile, R.id.iv_edit -> {
                closeDrawer()
                EditProfileDialogFragment.newInstance().show(supportFragmentManager, EditProfileDialogFragment.TAG)
            }

            R.id.tv_bank -> {
            }
//            R.id.tv_payment_option -> {
//            }
//            R.id.tv_invite_friends -> {
//            }
            R.id.tv_login_security -> {
            }
            R.id.tv_notification -> {
            }
            R.id.tv_help -> {
            }
            R.id.tv_legal_agreements -> {
            }
            R.id.tv_logout -> {
                performLogout()
            }
        }
    }

    private fun openDrawer() {
        drawer_layout.openDrawer(Gravity.START)
    }

    private fun closeDrawer() {
        drawer_layout.closeDrawer(Gravity.START)
    }

    private fun performLogout() {
        closeDrawer()
        val builder: AlertDialog.Builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = AlertDialog.Builder(this, R.style.AlertDialogTheme)
        } else {
            builder = AlertDialog.Builder(this)
        }
        builder.setTitle(R.string.log_out)
                .setMessage(getString(R.string.ques_want_to_logout))
                .setPositiveButton(android.R.string.yes, { dialog, which ->
                    updateUi(false)
                    logoutDisposable = apiService.logout(
                            access_token = SharedPreferenceUtil.getInstance(this).access_token
                    ).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    { onLogoutSuccess(it) },
                                    { onLogoutError(it) }
                            )
                })
                .setNegativeButton(android.R.string.no, { dialog, which ->
                    // do nothing
                })
                .show()
    }

    private fun updateUi(isEnable: Boolean) {
//        main_progress_bar.visibility = if (isEnable) View.GONE else View.VISIBLE
//        tv_send_money.isEnabled = isEnable
//        tv_request_money.isEnabled = isEnable
//        tv_wallet.isEnabled = isEnable
//        tv_activity.isEnabled = isEnable
    }

    private fun onLogoutSuccess(@Suppress("UNUSED_PARAMETER") baseResponse: BaseResponse) {
        updateUi(true)
        SharedPreferenceUtil.getInstance(this).deletePreferences()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun onLogoutError(throwable: Throwable) {
        updateUi(true)
        ErrorUtil.handlerGeneralError(this, drawer_layout, throwable)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_activity_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_notification -> {
            }
        }
        return true
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(Gravity.START)) {
            closeDrawer()
            return
        }

        if (backPressedTime + Constant.BACK_PRESS_TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed()
            return
        } else {
            ToastUtils.show(applicationContext, getString(R.string.message_double_tap_to_exit))
        }
        backPressedTime = System.currentTimeMillis()
    }

    override fun onDestroy() {
        logoutDisposable?.dispose()
        super.onDestroy()
    }

}