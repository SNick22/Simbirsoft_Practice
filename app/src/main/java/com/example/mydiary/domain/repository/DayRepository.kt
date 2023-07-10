package com.example.mydiary.domain.repository

import com.example.mydiary.domain.entity.DayEntity
import kotlinx.collections.immutable.PersistentList
import java.time.LocalDate

interface DayRepository {

    fun getDaysOfWeek(week: Int): List<DayEntity>

    fun getToday(): DayEntity

    fun getDayByData(day: Int, month: Int, year: Int): DayEntity

    fun getDayByTimestamp(timestamp: Long): DayEntity
}