package com.example.mydiary.domain

import com.example.mydiary.domain.entity.DayEntity
import com.example.mydiary.domain.repository.DayRepository
import javax.inject.Inject

class GetDaysOfWeekUseCase @Inject constructor(
    private val repository: DayRepository
) {

    operator fun invoke(week: Int): List<DayEntity> =
        repository.getDaysOfWeek(week)
}