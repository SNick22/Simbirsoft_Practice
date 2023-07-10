package com.example.mydiary.domain

import com.example.mydiary.domain.repository.TimeRepository
import javax.inject.Inject

class GetCurrentTimeUseCase @Inject constructor(
    private val repository: TimeRepository
) {

    operator fun invoke() = repository.getCurrentTime()
}