package app.android.snappay.customview

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet

class CustomTextView : AppCompatTextView {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    private fun init() {
    }
}