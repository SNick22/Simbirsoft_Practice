package com.example.mydiary

import android.app.Application
import com.example.mydiary.di.app.ApplicationComponent
import com.example.mydiary.di.app.DaggerApplicationComponent
import com.example.mydiary.utils.DaggerApplication

class App: Application(), DaggerApplication {

    private var component: ApplicationComponent? = null

    override fun onCreate() {
        component = DaggerApplicationComponent.factory()
            .create(this)

        super.onCreate()
    }

    override fun getAppComponent() = component
}