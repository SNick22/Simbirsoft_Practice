package com.example.mydiary.di.app

import android.content.Context
import com.example.mydiary.di.new_task.NewTaskDeps
import com.example.mydiary.di.task_details.TaskDetailsDeps
import com.example.mydiary.di.task_list.TaskListDeps
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        RoomModule::class,
        RepositoriesModule::class
    ]
)
interface ApplicationComponent:
    TaskListDeps,
    TaskDetailsDeps,
    NewTaskDeps
{

    @Component.Factory
    interface Factory {

        fun create(@BindsInstance context: Context): ApplicationComponent
    }
}