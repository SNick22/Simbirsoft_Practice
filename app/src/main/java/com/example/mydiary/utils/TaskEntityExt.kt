package com.example.mydiary.utils

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.mydiary.domain.entity.TaskEntity
import java.time.ZoneOffset

const val TIMESTAMP_DAY = 86400f

private fun getTrueTotalHeight(totalHeight: Float): Float = totalHeight - totalHeight / 25

fun TaskEntity.getTopMargin(totalHeight: Float, dayStartTime: Long): Dp {
    val trueTotalHeight = getTrueTotalHeight(totalHeight)
    val dif = dateStart - dayStartTime
    val startTime = if (dif < 0) 0f else dif.toFloat()
    return (startTime / TIMESTAMP_DAY * trueTotalHeight + totalHeight / 50).dp
}

fun TaskEntity.getHeight(totalHeight: Float, dayStartTime: Long, dayFinishTime: Long): Dp {
    val trueTotalHeight = getTrueTotalHeight(totalHeight)
    val dif = dateStart - dayStartTime
    val startTime = if (dif < 0) 0f else dif.toFloat()
    val finishTime = if (dateFinish > dayFinishTime) TIMESTAMP_DAY else TIMESTAMP_DAY - (dayFinishTime - dateFinish)
    val heightInTime = if (finishTime - startTime > 3600) finishTime - startTime else 3600f
    return (heightInTime / TIMESTAMP_DAY * trueTotalHeight).dp
}
