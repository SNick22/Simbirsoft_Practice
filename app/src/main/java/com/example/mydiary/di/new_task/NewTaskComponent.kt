package com.example.mydiary.di.new_task

import com.example.mydiary.presentation.screen.new_task.NewTaskViewModel
import com.example.mydiary.utils.BaseComponent
import dagger.Component

@NewTaskScope
@Component(
    dependencies = [NewTaskDeps::class]
)
interface NewTaskComponent: BaseComponent {

    fun getViewModel(): NewTaskViewModel

    @Component.Factory
    interface Factory {

        fun create(deps: NewTaskDeps): NewTaskComponent
    }
}