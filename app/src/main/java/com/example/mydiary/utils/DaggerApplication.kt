package com.example.mydiary.utils

import com.example.mydiary.di.app.ApplicationComponent

interface DaggerApplication {

    fun getAppComponent(): ApplicationComponent?
}