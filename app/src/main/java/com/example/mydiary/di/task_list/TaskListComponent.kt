package com.example.mydiary.di.task_list

import com.example.mydiary.presentation.screen.task_list.TaskListViewModel
import com.example.mydiary.utils.BaseComponent
import dagger.Component

@TaskListScope
@Component(
    dependencies = [TaskListDeps::class]
)
interface TaskListComponent: BaseComponent {

    fun getViewModel(): TaskListViewModel

    @Component.Factory
    interface Factory {

        fun create(deps: TaskListDeps): TaskListComponent
    }
}