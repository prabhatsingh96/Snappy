package app.android.snappay.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import app.android.snappay.util.BundleUtils

abstract class BaseFragment : Fragment() {

    companion object {
        @Suppress("unused")
        val TAG = BaseFragment::class.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BundleUtils.printBundleData(TAG, arguments)
    }

    abstract fun init()

    abstract fun initControl()

}
