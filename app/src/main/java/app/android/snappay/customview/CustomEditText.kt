package app.android.snappay.customview

import android.content.Context
import android.support.design.widget.TextInputEditText
import android.util.AttributeSet

class CustomEditText : TextInputEditText {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

}