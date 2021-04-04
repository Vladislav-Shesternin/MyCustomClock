package com.example.mycustomclock

// ------------------------------------------------------------| Time |
data class Time(
    var hour: Int = 0,
    var minute: Int = 0,
    var second: Int = 0,
)

fun Time.toSeconds(): Long {
    return (((hour * 60) + (minute * 60)) + second).toLong()
}

// ------------------------------------------------------------| TimeDegree |
private const val ONE_HOUR_IN_DEG = 30.0f
private const val ONE_MINUTE_IN_DEG = 6.0f
private const val ONE_SECOND_IN_DEG = 6.0f

data class TimeDegree(
    var hourDeg: Float = 0.0f,
    var minuteDeg: Float = 0.0f,
    var secondDeg: Float = 0.0f,
)

fun convertTimeToAngles(h: Int, m: Int, s: Int, block: (TimeDegree) -> Unit) {
    TimeDegree().apply {
        when {
            h != 0 -> hourDeg = h * ONE_HOUR_IN_DEG
            m != 0 -> minuteDeg = (h * 60 + m) * ONE_MINUTE_IN_DEG
            s != 0 -> secondDeg = (((h * 3600) + (m * 60)) + s) * ONE_SECOND_IN_DEG
        }

        block(this)
    }
}

