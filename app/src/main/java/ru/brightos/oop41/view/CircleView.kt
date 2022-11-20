package ru.brightos.oop41.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import ru.brightos.oop41.model.CCircle
import ru.brightos.oop41.utils.dp


class CircleView : SelectableView {

    private val circle: CCircle

    constructor(
        context: Context,
        attrs: AttributeSet? = null
    ) : super(context, attrs) {
        circle = CCircle(x = 0f, y = 0f, radius = 100f)
    }

    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        circle: CCircle,
        onSingleObjectSelectedListener: OnSingleObjectSelectedListener
    ) : super(
        context,
        attrs,
        DrawLayout.LayoutParams(
            width = (circle.radius * 2).toInt(),
            height = (circle.radius * 2).toInt(),
            marginLeft = circle.x.toInt(),
            marginTop = circle.y.toInt()
        ),
        onSingleObjectSelectedListener
    ) {
        this.circle = circle
    }

    override fun onDraw(canvas: Canvas?) {
        circle.radius.let {
            if (isSelected) {
                // Рисуем закрашенный круг
                canvas?.drawCircle(
                    it, it, it, super.selectedFillPaint
                )

                // А поверх него выделение
                canvas?.drawCircle(
                    it, it, it - 2.5.dp, super.selectedStrokePaint
                )
            } else
                canvas?.drawCircle(
                    it, it, it, super.defaultPaint
                )
        }
        super.onDraw(canvas)
    }

}