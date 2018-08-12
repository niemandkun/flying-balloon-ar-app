package com.niemandkun.balloon.util

import java.text.SimpleDateFormat
import java.util.*

fun formatSecondsForTimer(seconds: Float): String {
    val date = Date((seconds * 1000).toLong())
    return SimpleDateFormat("mm:ss:SS", Locale.getDefault()).format(date)
}
