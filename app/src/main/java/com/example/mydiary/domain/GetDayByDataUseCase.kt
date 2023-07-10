package com.example.mydiary.domain

import com.example.mydiary.domain.repository.DayRepository
import javax.inject.Inject

class GetDayByDataUseCase @Inject constructor(
    private val repository: DayRepository
) {

    operator fun invoke(day: Int, month: Int, year: Int) = repository.getDayByData(day, month, year)
}