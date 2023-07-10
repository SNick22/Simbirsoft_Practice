package com.example.mydiary.domain.entity

data class DayEntity (
    val day: Int,
    val dayOfWeek: Int,
    val month: Int,
    val year: Int,
    val timeStart: Long,
    val timeFinish: Long
)