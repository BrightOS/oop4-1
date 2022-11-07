package ru.brightos.oop41.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import ru.brightos.oop41.R

class EmojiView(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs), View.OnClickListener {

    var emojiSelectedAction: () -> Unit = { }
    var emojiDeselectedAction: () -> Unit = { }

    var text = ""
    var checked = false

    var tapCount: Int = 0
        set(value) {
            field = value
            requestLayout()
        }

    private var minTextLength = "\uD83E\uDD70"
    private var customSize = 0f
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 14f * getResources().getDisplayMetrics().scaledDensity
        textAlign = Paint.Align.CENTER
        typeface = ResourcesCompat.getFont(context, R.font.inter_regular)
    }

    private val textBounds = Rect()
    private val textCoordinate = PointF()
    private val tempFontMetrics = Paint.FontMetrics()

    init {
        val typedArray: TypedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.EmojiView
        )

        text = typedArray.getString(R.styleable.EmojiView_customText).orEmpty()
        if (text.length > minTextLength.length)
            minTextLength = text

        tapCount = typedArray.getInteger(R.styleable.EmojiView_tap_count, 0)
        customSize = typedArray.getDimension(
            R.styleable.EmojiView_textSize,
            14f * getResources().getDisplayMetrics().scaledDensity
        )

        textPaint.color =
            typedArray.getColor(R.styleable.EmojiView_customTextColor, Color.WHITE)

        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        textPaint.getTextBounds(
            (minTextLength + tapCount.toString()),
            0,
            (minTextLength + tapCount.toString()).length,
            textBounds
        )

        val textHeight = textBounds.height() + HEIGHT_STEP
        val textWidth = textBounds.width() + WIDTH_STEP

        val totalWidth = textWidth + paddingRight + paddingLeft
        val totalHeight = textHeight + paddingTop + paddingBottom

        val resultWidth = resolveSize(totalWidth, widthMeasureSpec)
        val resultHeight = resolveSize(totalHeight, heightMeasureSpec)

        setMeasuredDimension(resultWidth, resultHeight)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        textPaint.getFontMetrics(tempFontMetrics)
        textCoordinate.x = w / 2f
        textCoordinate.y = h / 2f + textBounds.height() / 2 - tempFontMetrics.descent
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawText(
            text,
            textCoordinate.x - TEXT_MARGIN * tapCount.toString().length,
            textCoordinate.y,
            textPaint
        )
        textPaint.textSize = customSize
        if (tapCount > 0)
            canvas.drawText(
                tapCount.toString(),
                textCoordinate.x + STEP_FORMAT_TEXT,
                textCoordinate.y,
                textPaint
            )
        else
            visibility = View.GONE
        textPaint.textSize = 14f * getResources().getDisplayMetrics().scaledDensity

        background = if (isSelected)
            AppCompatResources.getDrawable(context, R.drawable.emoji_selected)
        else
            AppCompatResources.getDrawable(context, R.drawable.emoji_deselected)
    }

    override fun onClick(view: View?) {
        isSelected = !isSelected
    }

    companion object {
        private const val HEIGHT_STEP = 48
        private const val WIDTH_STEP = 90
        private const val STEP_FORMAT_TEXT = 30
        private const val DEFAULT_SIZE = 42f
        private const val TEXT_MARGIN = 15
    }
}