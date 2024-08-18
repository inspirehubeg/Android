package ih.tools.readingpad.feature_book_parsing.domain

import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Paint.FontMetricsInt
import android.graphics.Path
import android.text.style.ReplacementSpan


open class DottedUnderlineSpan : ReplacementSpan {
    //override var id: Long = 0

    private var p: Paint
    private var mWidth = 0
    var mOffsetY: Int = 20
    private var mSpan: String
    var mStrokeWidth: Float = 10f
    var mDashPathEffect: Float = 20f
    private var mSpanLength = 0f
    private val mLengthIsCached = false

    constructor(_color: Int, _spannedText: String) {
        p = Paint()
        p.color = _color
        p.style = Paint.Style.STROKE
        p.setPathEffect(DashPathEffect(floatArrayOf(mDashPathEffect, mDashPathEffect), 0f))
        p.strokeWidth = mStrokeWidth
        mSpan = _spannedText
    }

    constructor(
        spannedText: String,
        color: Int,
        offsetY: Int,
        strokeWidth: Float,
        dashPathEffect: Float
    ) {
        mOffsetY = offsetY
        mStrokeWidth = strokeWidth
        mDashPathEffect = dashPathEffect
        p = Paint()
        p.color = color
        p.style = Paint.Style.FILL_AND_STROKE
        p.setPathEffect(DashPathEffect(floatArrayOf(mDashPathEffect, mDashPathEffect), 0f))
        p.strokeWidth = mStrokeWidth
        mSpan = spannedText
    }


    override fun getSize(
        paint: Paint,
        text: CharSequence,
        start: Int,
        end: Int,
        fm: FontMetricsInt?
    ): Int {
        mWidth = paint.measureText(text, start, end).toInt()
        return mWidth
    }

    override fun draw(
        canvas: Canvas,
        text: CharSequence,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        canvas.drawText(text, start, end, x, y.toFloat(), paint)
        if (!mLengthIsCached) mSpanLength = paint.measureText(mSpan)

        // https://code.google.com/p/android/issues/detail?id=29944
        // canvas.drawLine can't draw dashes when hardware acceleration is enabled,
        // but canvas.drawPath can
        val path = Path()
        path.moveTo(x, (y + mOffsetY).toFloat())
        path.lineTo(x + mSpanLength, (y + mOffsetY).toFloat())
        canvas.drawPath(path, this.p)
    }
}

