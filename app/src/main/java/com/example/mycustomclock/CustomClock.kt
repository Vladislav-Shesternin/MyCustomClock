package com.example.mycustomclock

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes

class CustomClock(
    context: Context,
    attrs: AttributeSet
) : View(context, attrs) {

    companion object {
        // Amount indicators of clock
        const val INDICATORS = 12

        // Keys
        const val VALUE_DEG_SECONDS = "seconds"
        const val VALUE_DEG_MINUTES = "minutes"
        const val VALUE_DEG_HOURS = "hours"

        // Colors
        const val DEFAULT_COLOR_CLOCK = Color.BLACK
        const val DEFAULT_COLOR_INDICATORS = Color.BLACK
        const val DEFAULT_COLOR_SECOND_HAND = Color.BLACK
        const val DEFAULT_COLOR_MINUTE_HAND = Color.BLACK
        const val DEFAULT_COLOR_HOUR_HAND = Color.BLACK
    }

    // Colors
    private var colorClock = DEFAULT_COLOR_CLOCK
    private var colorIndicators = DEFAULT_COLOR_INDICATORS
    private var colorSecondHand = DEFAULT_COLOR_SECOND_HAND
    private var colorMinuteHand = DEFAULT_COLOR_MINUTE_HAND
    private var colorHourHand = DEFAULT_COLOR_HOUR_HAND

    // Paint
    private val paint = Paint().apply {
        isAntiAlias = true
        strokeWidth = 5.0f
    }

    // Size
    private var width = 0.0f
    private var height = 0.0f
    private var spaceClock = 0.0f

    // Angles
    private var angleSeconds = 0.0f
    private var angleMinutes = 0.0f
    private var angleHours = 0.0f

    // Values
    private lateinit var valueSeconds: PropertyValuesHolder
    private lateinit var valueMinutes: PropertyValuesHolder
    private lateinit var valueHours: PropertyValuesHolder

    // Time
    private val time = Time()

    init {
        // Get data from styleable
        context.withStyledAttributes(attrs, R.styleable.CustomClock) {
            // Time
            time.apply {
                hour = getInt(R.styleable.CustomClock_hour, 0)
                minute = getInt(R.styleable.CustomClock_minute, 0)
                second = getInt(R.styleable.CustomClock_second, 0)
            }
            // Colors
            colorClock = getColor(R.styleable.CustomClock_colorClock, DEFAULT_COLOR_CLOCK)
            colorIndicators =
                getColor(R.styleable.CustomClock_colorIndicators, DEFAULT_COLOR_INDICATORS)
            colorSecondHand =
                getColor(R.styleable.CustomClock_colorSecondHand, DEFAULT_COLOR_SECOND_HAND)
            colorMinuteHand =
                getColor(R.styleable.CustomClock_colorMinuteHand, DEFAULT_COLOR_MINUTE_HAND)
            colorHourHand = getColor(R.styleable.CustomClock_colorHourHand, DEFAULT_COLOR_HOUR_HAND)
        }
        // Convert time to angles
        time.apply {
            convertTimeToAngles(hour, minute, second) {
                valueSeconds = PropertyValuesHolder.ofFloat(VALUE_DEG_SECONDS, 0.0f, it.secondDeg)
                valueMinutes = PropertyValuesHolder.ofFloat(VALUE_DEG_MINUTES, 0.0f, it.minuteDeg)
                valueHours = PropertyValuesHolder.ofFloat(VALUE_DEG_HOURS, 0.0f, it.hourDeg)
            }
        }
        // Update clock time
        updateTime(time.toSeconds())
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
        paint.apply {
            style = Paint.Style.STROKE
            color = colorClock
        }

        val cx = width / 2
        val cy = height / 2
        val radius = cx * 0.9f

        spaceClock = cx - radius

        canvas.drawCircle(cx, cy, radius, paint)

        drawIndicator(canvas, cx, cy, radius)
        drawSecondHand(canvas, cx, cy, radius)
        drawMinuteHand(canvas, cx, cy, radius)
        drawHourHand(canvas, cx, cy, radius)
        drawCenter(canvas, cx, cy, radius)
    }

    // ------------------------------| drawIndicator |
    private fun drawIndicator(canvas: Canvas, cxClock: Float, cyClock: Float, radiusClock: Float) {
        paint.apply {
            style = Paint.Style.FILL
            color = colorIndicators
        }

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
        paint.apply {
            strokeWidth = 5.0f
            color = colorSecondHand
        }

        val stopX = cxClock
        val stopY = radiusClock * 0.1f + spaceClock

        canvas.apply {
            save()

            rotate(angleSeconds, cxClock, cyClock)
            drawLine(cxClock, cyClock, stopX, stopY, paint)
        }
    }

    // ------------------------------| drawMinuteHand |
    private fun drawMinuteHand(canvas: Canvas, cxClock: Float, cyClock: Float, radiusClock: Float) {
        paint.apply {
            strokeWidth = 7.5f
            color = colorMinuteHand
        }

        val stopX = cxClock
        val stopY = radiusClock * 0.3f + spaceClock

        canvas.apply {
            restore()
            save()

            rotate(angleMinutes, cxClock, cyClock)
            drawLine(cxClock, cyClock, stopX, stopY, paint)
        }
    }

    // ------------------------------| drawHourHand |
    private fun drawHourHand(canvas: Canvas, cxClock: Float, cyClock: Float, radiusClock: Float) {
        paint.apply {
            strokeWidth = 10.0f
            color = colorHourHand
        }
        val stopX = cxClock
        val stopY = radiusClock / 2 + spaceClock

        canvas.apply {
            restore()
            rotate(angleHours, cxClock, cyClock)
            drawLine(cxClock, cyClock, stopX, stopY, paint)
        }
    }

    // ------------------------------| drawCenter |
    private fun drawCenter(canvas: Canvas, cxClock: Float, cyClock: Float, radiusClock: Float) {
        paint.apply {
            style = Paint.Style.FILL
            color = colorClock
        }

        val radius = radiusClock * 0.1f
        canvas.drawCircle(cxClock, cyClock, radius, paint)
    }

    // ------------------------------------------------------------| Functions |
    // ------------------------------| updateTime |
    private fun updateTime(seconds: Long) {
        ValueAnimator().apply {
            setValues(valueSeconds, valueMinutes, valueHours)
            duration = seconds * 1000

            addUpdateListener {
                angleSeconds = it.getAnimatedValue(VALUE_DEG_SECONDS) as Float
                angleMinutes = it.getAnimatedValue(VALUE_DEG_MINUTES) as Float
                angleHours = it.getAnimatedValue(VALUE_DEG_HOURS) as Float
                invalidate()
            }
        }.start()
    }
}

































