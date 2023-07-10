package com.example.mydiary.data.repository

import com.example.mydiary.data.mappers.toDateEntity
import com.example.mydiary.domain.entity.DayEntity
import com.example.mydiary.domain.repository.DayRepository
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.temporal.TemporalAdjusters
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class DayRepositoryImpl @Inject constructor(): DayRepository {

    private val timeZone = ZoneOffset.systemDefault()

    private fun getTimeStart(date: LocalDate) = date.atStartOfDay(timeZone).toEpochSecond()

    private fun getTimeFinish(date: LocalDate) = getTimeStart(date) + 86399

    override fun getDaysOfWeek(week: Int): List<DayEntity> {
        val currentDate = LocalDate.now().plusWeeks(week.toLong())

        val startOfWeek = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val endOfWeek = currentDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))

        val datesOfWeek = mutableListOf<DayEntity>()
        var date = startOfWeek
        while (!date.isAfter(endOfWeek)) {
            val timeStart = getTimeStart(date)
            val timeFinish = getTimeFinish(date)

            datesOfWeek.add(date.toDateEntity(timeStart, timeFinish))

            date = date.plusDays(1)
        }

        return datesOfWeek
    }

    override fun getToday(): DayEntity {
        val date = LocalDate.now()
        val timeStart = getTimeStart(date)
        val timeFinish = getTimeFinish(date)
        return date.toDateEntity(timeStart, timeFinish)
    }

    override fun getDayByData(day: Int, month: Int, year: Int): DayEntity {
        val date = LocalDate.of(year, month, day)
        val timeStart = getTimeStart(date)
        val timeFinish = getTimeFinish(date)
        return date.toDateEntity(timeStart, timeFinish)
    }

    override fun getDayByTimestamp(timestamp: Long): DayEntity {
        val calendar = Calendar.getInstance()
        calendar.time = Date.from(Instant.ofEpochSecond(timestamp))
        return getDayByData(
            day = calendar.get(Calendar.DAY_OF_MONTH),
            month = calendar.get(Calendar.MONTH) + 1,
            year = calendar.get(Calendar.YEAR)
        )
    }
}