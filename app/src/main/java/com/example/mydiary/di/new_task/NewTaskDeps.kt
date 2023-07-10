package com.example.mydiary.di.new_task

import com.example.mydiary.data.datasource.local.room.TaskDao
import com.example.mydiary.domain.repository.DayRepository
import com.example.mydiary.domain.repository.TaskRepository
import com.example.mydiary.domain.repository.TimeRepository

interface NewTaskDeps {

    fun taskDao(): TaskDao

    fun dayRepository(): DayRepository

    fun taskRepository(): TaskRepository

    fun timeRepository(): TimeRepository
}