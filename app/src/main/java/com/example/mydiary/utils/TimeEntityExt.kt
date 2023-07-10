package com.example.mydiary.utils

import com.example.mydiary.domain.entity.TimeEntity

fun TimeEntity.getNewTaskText(): String {
    val hourText = if (hour < 10) "0$hour" else hour.toString()
    val minuteText = if (minute < 10) "0$minute" else minute.toString()
    return "$hourText:$minuteText"
}
