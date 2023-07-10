package com.example.mydiary.di.task_list

import com.example.mydiary.data.datasource.local.room.TaskDao
import com.example.mydiary.domain.repository.DayRepository
import com.example.mydiary.domain.repository.TaskRepository

interface TaskListDeps {

    fun taskDao(): TaskDao

    fun dayRepository(): DayRepository

    fun taskRepository(): TaskRepository
}