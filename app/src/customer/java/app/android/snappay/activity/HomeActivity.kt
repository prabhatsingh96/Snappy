package app.android.snappay.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import app.android.snappay.R
import app.android.snappay.adapter.HomeAdapter
import app.android.snappay.constant.BundleConstant
import app.android.snappay.constant.Constant
import app.android.snappay.databinding.ActivityHomeBinding
import app.android.snappay.fragment.EditProfileDialogFragment
import app.android.snappay.model.request.UserDetailsRequest
import app.android.snappay.model.response.BaseResponse
import app.android.snappay.model.response.UploadProfilePicResponse
import app.android.snappay.model.response.UserDetailsResponse
import app.android.snappay.util.*
import com.google.zxing.integration.android.IntentIntegrator
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.customer.activity_home.*
import kotlinx.android.synthetic.main.fragment_custom_dialog.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class HomeActivity : BaseActivity(), View.OnClickListener, EditProfileDialogFragment.OnItemClickListener {

    private var backPressedTime: Long = 0

    private var getUserDetailsDisposable: Disposable? = null
    private var logoutDisposable: Disposable? = null
    private var uploadProfilePicDisposable: Disposable? = null
    private var removeProfilePicDisposable: Disposable? = null

    private lateinit var currentImageUri: Uri
    private lateinit var binding: ActivityHomeBinding

    companion object {
        var TAG = HomeActivity::class.simpleName
        private const val RC_PERM_PICK_IMAGE = 201
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
                imageView = iv_profile,
                isCircleCrop = true,
                load = R.drawable.ic_profile_placeholder,
                placeholderResId = R.drawable.ic_profile_placeholder,
                errorResId = R.drawable.ic_profile_placeholder
        )

        tv_drawer_greeting_msg.text = TimeUtils.getGreetingMessage(this)
        tv_drawer_first_name.text = SharedPreferenceUtil.getInstance(this).first_name

        tv_first_name.text = SharedPreferenceUtil.getInstance(this).first_name
        tv_greeting_msg.text = TimeUtils.getGreetingMessage(this)

        rv_home.adapter = HomeAdapter(this)
    }

    override fun initControl() {
        /*Main container*/
        tv_send_money.setOnClickListener(this)
        tv_request_money.setOnClickListener(this)
        tv_wallet.setOnClickListener(this)
        tv_activity.setOnClickListener(this)

        /*Drawer items*/

        iv_drawer_profile.setOnClickListener(this)
        iv_edit.setOnClickListener(this)

        tv_bank_card.setOnClickListener(this)
        tv_payment_option.setOnClickListener(this)
        tv_invite_friends.setOnClickListener(this)
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

            R.id.tv_send_money -> {
                startActivity(Intent(this, SendMoneyActivity::class.java))
            }

            R.id.tv_request_money -> {
               startActivity(Intent(this, RequestMoneyActivity::class.java))
            }

            R.id.tv_wallet -> {
                startActivity(Intent(this, WalletActivity::class.java))
            }
            R.id.tv_activity -> {
            }

        /*Drawer items*/
            R.id.iv_drawer_profile, R.id.iv_edit -> {
                closeDrawer()
                EditProfileDialogFragment.newInstance().show(supportFragmentManager, EditProfileDialogFragment.TAG)
            }

            R.id.tv_bank_card -> {
            }
            R.id.tv_payment_option -> {
            }
            R.id.tv_invite_friends -> {
            }
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
        main_progress_bar.visibility = if (isEnable) View.GONE else View.VISIBLE
        tv_send_money.isEnabled = isEnable
        tv_request_money.isEnabled = isEnable
        tv_wallet.isEnabled = isEnable
        tv_activity.isEnabled = isEnable
    }

    private fun onLogoutSuccess(@Suppress("UNUSED_PARAMETER") baseResponse: BaseResponse) {
        updateUi(true)
        forceLogout()
    }

    private fun onLogoutError(throwable: Throwable) {
        updateUi(true)
        forceLogout()
    }

    /**
     * Perform logout for both the success and error case (force logout)
     */
    private fun forceLogout() {
        SharedPreferenceUtil.getInstance(this).deletePreferences()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun onFetchUserDetailsSuccess(userDetailsResponse: UserDetailsResponse) {
        updateUi(true)
        startActivity(
                Intent(this, EnterAmountActivity::class.java).apply {
                    putExtra(BundleConstant.USER_BEAN, userDetailsResponse.response)
                }
        )
    }

    private fun onFetchUserDetailsError(throwable: Throwable) {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        IntentIntegrator.parseActivityResult(requestCode, resultCode, data)?.takeIf { it.contents != null }?.let {
            updateUi(false)
            getUserDetailsDisposable = apiService.getUserDetails(
                    access_token = SharedPreferenceUtil.getInstance(this).access_token
                    , userDetailsRequest = UserDetailsRequest(it.contents)
            ).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { onFetchUserDetailsSuccess(it) },
                            { onFetchUserDetailsError(it) }
                    )
        }

        when (requestCode) {
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                when (resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        currentImageUri = CropImage.getActivityResult(data).uri
                        Log.i(TAG, "currentImageUri: $currentImageUri")
                        if (::currentImageUri.isInitialized) {
                            updateUi(false)
                            val selectedFile = File(currentImageUri.path)
                            uploadProfilePicDisposable = apiService.uploadProfilePic(
                                    SharedPreferenceUtil.getInstance(this).access_token
                                    , MultipartBody.Part.createFormData("profile_image", selectedFile.name, RequestBody.create(MediaType.parse("image/*"), selectedFile))
                            )
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                            { onUploadProfilePicSuccess(it) },
                                            { onUploadProfilePicFailure(it) }
                                    )
                        }
                    }
                    CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE -> {
                        val error = CropImage.getActivityResult(data).error
                        Log.e(TAG, error.message)
                    }
                }
            }
        }
    }

    private fun onUploadProfilePicSuccess(uploadProfilePicResponse: UploadProfilePicResponse) {
        updateUi(true)
    }

    private fun onUploadProfilePicFailure(throwable: Throwable) {
        updateUi(true)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            RC_PERM_PICK_IMAGE ->
                if (grantResults.size >= 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    selectImage()
                } else {
                    SnackbarUtils.displaySnackbar(cv_root, getString(R.string.error_perm_required_for_update_profile_pic))
                }
        }
    }

    /**
     * EditProfileDialogFragment.OnItemClickListener
     */
    override fun onSelectImage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), RC_PERM_PICK_IMAGE)
        } else {
            selectImage()
        }
    }

    override fun onRemoveImage() {
        updateUi(false)
        removeProfilePicDisposable = apiService.removeProfilePic(
                SharedPreferenceUtil.getInstance(this).access_token
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { onRemoveProfilePicSuccess(it) },
                        { onRemoveProfilePicFailure(it) }
                )
    }

    private fun onRemoveProfilePicSuccess(baseResponse: BaseResponse) {
        updateUi(true)
    }

    private fun onRemoveProfilePicFailure(throwable: Throwable) {
        updateUi(true)
        ErrorUtil.handlerGeneralError(this, drawer_layout, throwable)
    }

    private fun selectImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setFixAspectRatio(true)
                .start(this)
    }

    override fun onDestroy() {
        logoutDisposable?.dispose()
        uploadProfilePicDisposable?.dispose()
        getUserDetailsDisposable?.dispose()
        super.onDestroy()
    }

}