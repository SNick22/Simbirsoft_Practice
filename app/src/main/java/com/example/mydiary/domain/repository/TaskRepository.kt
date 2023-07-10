package com.example.mydiary.domain.repository

import com.example.mydiary.domain.entity.TaskEntity

interface TaskRepository {

    suspend fun getTasks(dateStart: Long, dateFinish: Long): List<TaskEntity>

    suspend fun addTask(task: TaskEntity)

    suspend fun getTask(taskId: Int): TaskEntity

    suspend fun saveMocks()

    suspend fun deleteTask(task: TaskEntity)
}