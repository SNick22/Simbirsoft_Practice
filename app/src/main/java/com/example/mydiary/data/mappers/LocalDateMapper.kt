package com.example.mydiary.data.mappers

import com.example.mydiary.domain.entity.DayEntity
import java.time.LocalDate

fun LocalDate.toDateEntity(timeStart: Long, timeFinish: Long): DayEntity =
    DayEntity(
        day = dayOfMonth,
        dayOfWeek = dayOfWeek.value,
        month = monthValue,
        year = year,
        timeStart = timeStart,
        timeFinish = timeFinish
    )