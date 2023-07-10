package com.example.mydiary.domain

import com.example.mydiary.domain.repository.TaskRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetTasksUseCase @Inject constructor(
    private val repository: TaskRepository
) {

    suspend operator fun invoke(dateStart: Long, dateFinish: Long) =
        withContext(Dispatchers.IO) {
            repository.getTasks(dateStart, dateFinish)
        }
}