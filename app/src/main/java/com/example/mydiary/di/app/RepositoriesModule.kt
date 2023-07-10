package com.example.mydiary.di.app

import com.example.mydiary.data.repository.DayRepositoryImpl
import com.example.mydiary.data.repository.TaskRepositoryImpl
import com.example.mydiary.data.repository.TimeRepositoryImpl
import com.example.mydiary.domain.repository.DayRepository
import com.example.mydiary.domain.repository.TaskRepository
import com.example.mydiary.domain.repository.TimeRepository
import dagger.Binds
import dagger.Module

@Module
interface RepositoriesModule {

    @Binds
    fun bindDayRepository(repositoryImpl: DayRepositoryImpl): DayRepository

    @Binds
    fun bindTaskRepository(repositoryImpl: TaskRepositoryImpl): TaskRepository

    @Binds
    fun bindTimeRepository(repositoryImpl: TimeRepositoryImpl): TimeRepository
}