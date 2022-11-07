package ru.brightos.oop41.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import ru.brightos.oop41.utils.dp


class CCircleView : View {

    private val radius: Float
    private val circlePaint: Paint
    private val selectedCirclePaint: Paint
    private val circleSelectedAction: (CCircleView) -> Unit

    init {
        println("init")
        circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = Color.parseColor("#3E94D1")
        }
        selectedCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = Color.parseColor("#65A5D1")
            style = Paint.Style.STROKE
            color = Color.parseColor("#5CCDC9")
            strokeWidth = 5.dp
        }
    }

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {
        println("contructor without args")
        radius = 100f
        circleSelectedAction = {}
    }

    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        radius: Float,
        circleSelectedAction: (CCircleView) -> Unit
    ) : super(context, attrs) {
        println("contructor with args")
        this.radius = radius
        this.circleSelectedAction = circleSelectedAction
    }

    override fun onDraw(canvas: Canvas?) {
        if (isSelected)
            canvas?.drawCircle(
                radius, radius, radius - 2.5.dp, selectedCirclePaint
            )
        else
            canvas?.drawCircle(
                radius, radius, radius, circlePaint
            )
        println("$left $top $right $bottom")
    }

    val LONG_PRESS_DELAY = 0
    var boundaries: Rect? = null
    var mHandler = Handler(Looper.myLooper()!!)

    var onTap = Runnable {
        println("Tapped")
        handler.postDelayed(
            onLongPress,
            LONG_PRESS_DELAY - ViewConfiguration.getTapTimeout().toLong()
        )
    }

    var onLongPress = Runnable {
        println("Long Pressed")
        isSelected = !isSelected
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                boundaries = Rect(left, top, right, bottom)
                println(boundaries)
                mHandler.postDelayed(onTap, ViewConfiguration.getTapTimeout().toLong())
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                mHandler.removeCallbacks(onLongPress)
                mHandler.removeCallbacks(onTap)
            }
            MotionEvent.ACTION_MOVE -> {
                if (!boundaries!!.contains(
                        left + event.x.toInt(),
                        top + event.y.toInt()
                    )
                ) {
                    mHandler.removeCallbacks(onLongPress)
                    mHandler.removeCallbacks(onTap)
                } else
                    println("Contains!")
            }
        }
        return true
    }
}