package com.example.mydiary.di.app

import android.content.Context
import androidx.room.Room
import com.example.mydiary.data.datasource.local.room.AppDatabase
import com.example.mydiary.data.datasource.local.room.TaskDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule {

    @Provides
    @Singleton
    fun provideAppDatabase(context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, name = "MyDiaryDB")
            .build()

    @Provides
    fun provideTaskDao(appDatabase: AppDatabase): TaskDao =
        appDatabase.getTaskDao()
}