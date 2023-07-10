package com.example.mydiary.di.task_details

import com.example.mydiary.presentation.screen.task_details.TaskDetailsViewModel
import com.example.mydiary.utils.BaseComponent
import dagger.Component

@TaskDetailsScope
@Component(
    dependencies = [TaskDetailsDeps::class]
)
interface TaskDetailsComponent: BaseComponent {

    fun getViewModelFactory(): TaskDetailsViewModel.Factory

    @Component.Factory
    interface Factory {

        fun create(deps: TaskDetailsDeps): TaskDetailsComponent
    }
}