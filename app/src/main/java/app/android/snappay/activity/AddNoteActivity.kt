package app.android.snappay.activity

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.text.Selection
import android.view.View
import app.android.snappay.R
import app.android.snappay.R.id.*
import app.android.snappay.constant.BundleConstant
import app.android.snappay.databinding.ActivityAddNoteBinding
import app.android.snappay.util.KeyboardUtils
import app.android.snappay.util.ToastUtils
import kotlinx.android.synthetic.main.activity_add_note.*

class AddNoteActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAddNoteBinding

    private val note: String
        get() = intent.getStringExtra(BundleConstant.NOTE)

    companion object {
        var TAG = AddNoteActivity::class.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_note)
        init(savedInstanceState)
        initControl()
    }

    override fun init(savedInstanceState: Bundle?) {
        setSupportActionBar(toolbar)
        et_note.setText(note)
        Selection.setSelection(et_note.text, et_note.text.length)
    }

    override fun initControl() {
        fl_root.setOnClickListener(this)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        btn_save.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fl_root -> KeyboardUtils.hideKeyboard(this)
            R.id.btn_save -> {
                ToastUtils.show(this, getString(R.string.message_note_added))
                Intent().apply {
                    putExtra(BundleConstant.NOTE, et_note.text.toString())
                    setResult(RESULT_OK, this)
                }
                finish()
            }
        }
    }
}