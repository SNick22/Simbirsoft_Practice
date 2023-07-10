package com.example.mydiary.domain

import com.example.mydiary.domain.repository.DayRepository
import javax.inject.Inject

class GetTodayUseCase @Inject constructor(
    private val repository: DayRepository
) {

    operator fun invoke() = repository.getToday()
}