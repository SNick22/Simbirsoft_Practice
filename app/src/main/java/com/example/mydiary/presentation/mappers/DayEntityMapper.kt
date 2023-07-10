package com.example.mydiary.presentation.mappers

import com.example.mydiary.domain.entity.DayEntity
import java.time.LocalDate

fun DayEntity.toLocalDate(): LocalDate = LocalDate.of(year, month, day)
