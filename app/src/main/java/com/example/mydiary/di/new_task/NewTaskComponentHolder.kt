package com.example.mydiary.di.new_task

import android.content.Context
import com.example.mydiary.utils.ComponentHolder
import com.example.mydiary.utils.DaggerApplication

object NewTaskComponentHolder: ComponentHolder<NewTaskComponent>() {

    override fun build(context: Context): NewTaskComponent =
        DaggerNewTaskComponent.factory()
            .create((context.applicationContext as DaggerApplication).getAppComponent()!!)
}