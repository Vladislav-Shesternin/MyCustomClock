package com.example.mycustomclock

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.transform

private const val HOURS = 12

class CustomClock(
    context: Context,
    attrs: AttributeSet
) : View(context, attrs) {

    // Paint
    private val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 3.0f
    }

    // Size
    private var width = 0.0f
    private var height = 0.0f

    // Radius
    private var radius = 0.0f

    // ------------------------------------------------------------| Overrides |
    // ------------------------------| onSizeChanged |
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        width = (if (w < h) w else h).toFloat()
        height = width
    }

    // ------------------------------| onDraw |
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawClock(canvas)
    }

    // ------------------------------------------------------------| Draw functions |
    // ------------------------------| drawClock |
    private fun drawClock(canvas: Canvas) {
        val cx = width / 2
        val cy = height / 2
        radius = cx * 0.9f

        canvas.drawCircle(cx, cy, radius, paint)
        drawIndicator(canvas, cx, cy, radius)
    }

    // ------------------------------| drawIndicator |
    private fun drawIndicator(canvas: Canvas, cxClock: Float, cyClock: Float, radiusClock: Float) {
        paint.style = Paint.Style.FILL

        val cx = cxClock
        val cy = cyClock - radiusClock
        radius = cy / 2

        repeat(HOURS) {
            canvas.apply {
                rotate(30.0f, cxClock, cyClock)
                drawCircle(cx, cy, radius, paint)
            }
        }
    }

    // ------------------------------| drawMinuteHand |
    // ------------------------------| drawHourHand |
}

































