package com.example.mydiary.domain

import com.example.mydiary.domain.repository.DayRepository
import javax.inject.Inject

class GetDayByTimestampUseCase @Inject constructor(
    private val repository: DayRepository
) {

    operator fun invoke(timestamp: Long) = repository.getDayByTimestamp(timestamp)
}