package ru.brightos.oop41.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewManager
import androidx.annotation.AttrRes
import androidx.coordinatorlayout.widget.CoordinatorLayout

class MyCoordinatorLayout : CoordinatorLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attr: AttributeSet?) : super(context, attr)
    constructor(context: Context, attr: AttributeSet?, @AttrRes defStyleAttr: Int) : super(
        context,
        attr,
        defStyleAttr
    )

    var onTouchEnabled = true

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        if (onTouchEnabled) {
            onTouchAction(ev)
        }
        return false
    }

    private var onTouchAction: (MotionEvent) -> Unit = {}

    fun setOnTouchAction(l: (MotionEvent) -> Unit) {
        onTouchAction = l
    }

//    override fun onTouchEvent(ev: MotionEvent): Boolean {
//        if (onTouchEnabled) {
//            onTouchAction(ev)
//        }
//        return super.onTouchEvent(ev)
//    }
}