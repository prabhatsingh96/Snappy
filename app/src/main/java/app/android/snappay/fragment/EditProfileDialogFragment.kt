package app.android.snappay.fragment

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.android.snappay.R
import app.android.snappay.databinding.FragmentCustomDialogBinding
import kotlinx.android.synthetic.main.fragment_custom_dialog.*

class EditProfileDialogFragment : BaseDialogFragment(), View.OnClickListener {

    interface OnItemClickListener {
        fun onSelectImage()
        fun onRemoveImage()
    }

    private lateinit var binding: FragmentCustomDialogBinding

    companion object {
        @Suppress("unused")
        val TAG = EditProfileDialogFragment::class.simpleName

        fun newInstance() = EditProfileDialogFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_custom_dialog, container, false)
        return binding.root
    }

    private lateinit var onItemClickListener: EditProfileDialogFragment.OnItemClickListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onItemClickListener = context as OnItemClickListener
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
        initControl()
    }

    override fun init() {
    }

    override fun initControl() {
        iv_camera.setOnClickListener(this)
        iv_remove.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.iv_camera -> {
                dismiss()
                if (::onItemClickListener.isInitialized) {
                    onItemClickListener.onSelectImage()
                }
            }
            R.id.iv_remove -> {
                dismiss()
                if (::onItemClickListener.isInitialized) {
                    onItemClickListener.onRemoveImage()
                }
            }

        }
    }
}