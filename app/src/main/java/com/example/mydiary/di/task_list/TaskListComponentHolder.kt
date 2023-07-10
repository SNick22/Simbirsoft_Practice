package com.example.mydiary.di.task_list

import android.content.Context
import com.example.mydiary.utils.ComponentHolder
import com.example.mydiary.utils.DaggerApplication

object TaskListComponentHolder: ComponentHolder<TaskListComponent>() {

    override fun build(context: Context): TaskListComponent =
        DaggerTaskListComponent.factory()
            .create((context.applicationContext as DaggerApplication).getAppComponent()!!)
}