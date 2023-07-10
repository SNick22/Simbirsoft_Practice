package com.example.mydiary.data.mappers

import com.example.mydiary.domain.entity.TimeEntity
import java.time.LocalTime

fun LocalTime.toTimeEntity(): TimeEntity =
    TimeEntity(
        hour = hour,
        minute = minute,
        second = second
    )
