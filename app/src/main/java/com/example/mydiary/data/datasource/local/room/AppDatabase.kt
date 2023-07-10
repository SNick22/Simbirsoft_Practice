package com.example.mydiary.data.datasource.local.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TaskDataEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getTaskDao(): TaskDao
}