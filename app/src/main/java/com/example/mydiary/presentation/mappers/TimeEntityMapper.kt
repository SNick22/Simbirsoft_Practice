package com.example.mydiary.presentation.mappers

import com.example.mydiary.domain.entity.TimeEntity
import java.time.LocalTime

fun TimeEntity.toLocalTime(): LocalTime = LocalTime.of(hour, minute, second)
