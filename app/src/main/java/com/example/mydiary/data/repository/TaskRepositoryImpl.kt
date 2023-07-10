package com.example.mydiary.data.repository

import com.example.mydiary.data.datasource.local.mock.MockDataSource
import com.example.mydiary.data.datasource.local.room.TaskDao
import com.example.mydiary.data.mappers.toTaskDataEntity
import com.example.mydiary.data.mappers.toTaskEntity
import com.example.mydiary.domain.entity.TaskEntity
import com.example.mydiary.domain.repository.TaskRepository
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao,
    private val mockDataSource: MockDataSource
): TaskRepository {

    override suspend fun getTasks(dateStart: Long, dateFinish: Long): List<TaskEntity> =
        taskDao.getTasks(dateStart, dateFinish).map {
            it.toTaskEntity()
        }

    override suspend fun addTask(task: TaskEntity) {
        taskDao.save(task.toTaskDataEntity())
    }

    override suspend fun getTask(taskId: Int): TaskEntity = taskDao.getTask(taskId).toTaskEntity()

    override suspend fun saveMocks() {
        val tasks = mockDataSource.getMock().map { it.toTaskDataEntity() }
        taskDao.saveAll(tasks)
    }

    override suspend fun deleteTask(task: TaskEntity) {
        taskDao.deleteTask(task.toTaskDataEntity())
    }
}