package com.example.mydiary.domain

import com.example.mydiary.domain.entity.TaskEntity
import com.example.mydiary.domain.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {

    suspend operator fun invoke(task: TaskEntity) {
        withContext(Dispatchers.IO) {
            repository.deleteTask(task)
        }
    }
}