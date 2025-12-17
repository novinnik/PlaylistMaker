package com.practicum.playlistmaker.util

import android.content.Context
import android.util.TypedValue
import java.text.SimpleDateFormat
import java.util.Locale

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
}