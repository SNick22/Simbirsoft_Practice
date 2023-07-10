package com.example.mydiary.utils

import android.util.Log
import com.example.mydiary.domain.entity.DayEntity
import java.time.LocalDate
import kotlin.IllegalStateException

private fun getDayOfWeekName(week: Int): String =
    when(week) {
        1 -> "Понедельник"
        2 -> "Вторник"
        3 -> "Среда"
        4 -> "Четверг"
        5 -> "Пятница"
        6 -> "Суббота"
        7 -> "Воскресенье"
        else -> throw IllegalStateException("Not existed week $week")
    }

fun getMonthName(month: Int): String =
    when(month) {
        1 -> "января"
        2 -> "февраля"
        3 -> "марта"
        4 -> "апреля"
        5 -> "мая"
        6 -> "июня"
        7 -> "июля"
        8 -> "августа"
        9 -> "сентября"
        10 -> "октября"
        11 -> "ноября"
        12 -> "декабря"
        else -> throw IllegalStateException("Not existed month $month")
    }

fun DayEntity.toToDoListString(): String {
    Log.d("day", this.toString())

    val dayOfWeekName = getDayOfWeekName(dayOfWeek)
    val monthName = getMonthName(month)

    return "$dayOfWeekName $day $monthName $year г."
}