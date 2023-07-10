package com.example.mydiary.di.task_details

import android.content.Context
import com.example.mydiary.utils.ComponentHolder
import com.example.mydiary.utils.DaggerApplication

object TaskDetailsComponentHolder: ComponentHolder<TaskDetailsComponent>() {

    override fun build(context: Context): TaskDetailsComponent =
        DaggerTaskDetailsComponent.factory()
            .create((context.applicationContext as DaggerApplication).getAppComponent()!!)
}