package com.example.mycustomclock

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.transform

private const val INDICATORS = 12
private const val ANGLES_VALUE = "angles"

class CustomClock(
    context: Context,
    attrs: AttributeSet
) : View(context, attrs) {

    // Paint
    private val paint = Paint().apply {
        isAntiAlias = true
        color = Color.BLACK
        strokeWidth = 3.0f
    }

    // Size
    private var width = 0.0f
    private var height = 0.0f
    private var spaceClock = 0.0f

    var angleSeconds = 0.0f

    init {
        val angleValues = PropertyValuesHolder.ofFloat(ANGLES_VALUE, 0.0f, 360.0f)

        ValueAnimator().apply {
            addUpdateListener {
                angleSeconds = it.getAnimatedValue(ANGLES_VALUE) as Float
                invalidate()
            }
            setValues(angleValues)
            duration = 60000
        }.start()
    }


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
        paint.style = Paint.Style.STROKE

        val cx = width / 2
        val cy = height / 2
        val radius = cx * 0.9f

        spaceClock = cx - radius

        canvas.drawCircle(cx, cy, radius, paint)

        drawCenter(canvas, cx, cy, radius)
        drawIndicator(canvas, cx, cy, radius)
        drawHourHand(canvas, cx, cy, radius)
        drawMinuteHand(canvas, cx, cy, radius)
        drawSecondHand(canvas, cx, cy, radius)
    }

    // ------------------------------| drawCenter |
    private fun drawCenter(canvas: Canvas, cxClock: Float, cyClock: Float, radiusClock: Float) {
        paint.style = Paint.Style.FILL

        val radius = radiusClock * 0.1f
        canvas.drawCircle(cxClock, cyClock, radius, paint)
    }

    // ------------------------------| drawIndicator |
    private fun drawIndicator(canvas: Canvas, cxClock: Float, cyClock: Float, radiusClock: Float) {
        paint.style = Paint.Style.FILL

        val cx = cxClock
        val cy = cyClock - radiusClock
        val radius = cy / 2

        repeat(INDICATORS) {
            canvas.apply {
                rotate(30.0f, cxClock, cyClock)
                drawCircle(cx, cy, radius, paint)
            }
        }
    }

    // ------------------------------| drawSecondHand |
    private fun drawSecondHand(canvas: Canvas, cxClock: Float, cyClock: Float, radiusClock: Float) {
        paint.strokeWidth = 5.0f

        val stopX = cxClock
        val stopY = radiusClock * 0.1f + spaceClock

        canvas.rotate(angleSeconds, cxClock, cyClock)
        canvas.drawLine(cxClock, cyClock, stopX, stopY, paint)
    }

    // ------------------------------| drawMinuteHand |
    private fun drawMinuteHand(canvas: Canvas, cxClock: Float, cyClock: Float, radiusClock: Float) {
        paint.strokeWidth = 7.5f

        val stopX = cxClock
        val stopY = radiusClock * 0.3f + spaceClock

        canvas.drawLine(cxClock, cyClock, stopX, stopY, paint)

    }

    // ------------------------------| drawHourHand |
    private fun drawHourHand(canvas: Canvas, cxClock: Float, cyClock: Float, radiusClock: Float) {
        paint.strokeWidth = 10.0f

        val stopX = cxClock
        val stopY = radiusClock / 2 + spaceClock

        canvas.drawLine(cxClock, cyClock, stopX, stopY, paint)
    }
}

































