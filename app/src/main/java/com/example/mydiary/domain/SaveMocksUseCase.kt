package com.example.mydiary.domain

import com.example.mydiary.domain.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SaveMocksUseCase @Inject constructor(
    private val repository: TaskRepository
) {

    suspend operator fun invoke() {
        withContext(Dispatchers.IO) {
            repository.saveMocks()
        }
    }
}