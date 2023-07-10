package com.example.mydiary.di.task_details

import com.example.mydiary.data.datasource.local.room.TaskDao
import com.example.mydiary.domain.repository.DayRepository
import com.example.mydiary.domain.repository.TaskRepository

interface TaskDetailsDeps {

    fun taskDao(): TaskDao

    fun dayRepository(): DayRepository

    fun taskRepository(): TaskRepository
}