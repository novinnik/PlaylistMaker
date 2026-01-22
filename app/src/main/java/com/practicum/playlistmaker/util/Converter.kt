package com.practicum.playlistmaker.util

import android.content.Context
import android.util.TypedValue
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.abs

object Converter {

    fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }

    fun timeConversion(time: Long?): String {
        if (time == null) return "00:00"
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)
    }

    fun getNoun(number: Int, one: String, two: String, five: String): String {
        var n = abs(number)
        n %= 100
        if (n >= 5 && n <= 20) {
            return five
        }
        n %= 10
        if (n == 1) {
            return one
        }
        if (n >= 2 && n <= 4) {
            return two
        }
        return five
    }
}