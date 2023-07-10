package com.example.mydiary.utils

import java.time.LocalDate

fun LocalDate.getNewTaskText(): String {
    val monthName = getMonthName(monthValue)
    return "$dayOfMonth $monthName $year г."
}
