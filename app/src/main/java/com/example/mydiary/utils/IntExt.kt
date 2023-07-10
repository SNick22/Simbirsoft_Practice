package com.example.mydiary.utils


fun Int.toTasksTime(): String {
    val hours = this - (this / 24) * 24
    return when(hours) {
        in 0..9 -> "0$hours:00"
        else -> "$hours:00"
    }
}
