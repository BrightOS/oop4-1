package ru.brightos.oop41.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import ru.brightos.oop41.utils.dp


open class SelectableView : View {

    internal val defaultPaint: Paint
    internal val selectedFillPaint: Paint
    internal val selectedStrokePaint: Paint

    private val onSingleObjectSelectedListener: OnSingleObjectSelectedListener?

    init {
        println("init")
        defaultPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = Color.parseColor("#3E94D1")
        }
        selectedFillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = Color.parseColor("#2a78b0")
        }
        selectedStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            color = Color.parseColor("#67abdb")
            strokeWidth = 5.dp
        }
        isSelected = true
    }

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {
        this.onSingleObjectSelectedListener = null
    }
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        layoutParams: DrawLayout.LayoutParams,
        onSingleObjectSelectedListener: OnSingleObjectSelectedListener
    ) : super(context, attrs) {
        this.layoutParams = layoutParams
        this.onSingleObjectSelectedListener = onSingleObjectSelectedListener
    }

    override fun onDraw(canvas: Canvas?) {
        println("$left $top $right $bottom")
    }

    val LONG_PRESS_DELAY = 50
    var boundaries: Rect? = null
    var mHandler = Handler(Looper.myLooper()!!)
    var longClickPerformed = false

    var onTap = Runnable {
        println("Tapped")
        handler.postDelayed(
            onLongPress,
            LONG_PRESS_DELAY - ViewConfiguration.getTapTimeout().toLong()
        )
    }

    var onLongPress = Runnable {
        println("Long Pressed")
        select()
        longClickPerformed = true
        invalidate()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                longClickPerformed = false
                boundaries = Rect(left, top, right, bottom)
                println(boundaries)
                mHandler.postDelayed(onTap, ViewConfiguration.getTapTimeout().toLong())
                return true
            }
            MotionEvent.ACTION_CANCEL -> {
                mHandler.removeCallbacks(onLongPress)
                mHandler.removeCallbacks(onTap)
            }
            MotionEvent.ACTION_UP -> {
                mHandler.removeCallbacks(onLongPress)
                mHandler.removeCallbacks(onTap)
                println("$longClickPerformed $isSelected")
                if (!longClickPerformed && !isSelected) {
                    onSingleObjectSelectedListener?.onSingleObjectSelected()
                    isSelected = true
                    invalidate()
                }
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                if (!boundaries!!.contains(
                        left + event.x.toInt(),
                        top + event.y.toInt()
                    )
                ) {
                    mHandler.removeCallbacks(onLongPress)
                    mHandler.removeCallbacks(onTap)
                }
                return true
            }
        }
        return false
    }

    fun deleteView(): Boolean {
        if (isSelected)
            (parent as ViewGroup).removeView(this)
        return isSelected
    }

    fun select() {
        isSelected = true
    }

    fun deselect() {
        isSelected = false
    }
}