package app.android.snappay.customview

import android.content.Context
import android.support.v7.widget.AppCompatButton
import android.util.AttributeSet

class CustomButton : AppCompatButton {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
}