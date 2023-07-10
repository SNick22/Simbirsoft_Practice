package com.example.mydiary.data.datasource.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TaskDao {

    @Insert(entity = TaskDataEntity::class)
    suspend fun save(taskDataEntity: TaskDataEntity)

    @Insert(entity = TaskDataEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveAll(tasks: List<TaskDataEntity>)

    @Query("SELECT * FROM ${TaskDataEntity.TABLE_NAME} WHERE (date_start BETWEEN :dateStart AND :dateFinish) OR (:dateStart BETWEEN date_start AND date_finish)")
    suspend fun getTasks(dateStart: Long, dateFinish: Long): List<TaskDataEntity>

    @Query("SELECT * FROM ${TaskDataEntity.TABLE_NAME} WHERE id = :taskId")
    suspend fun getTask(taskId: Int): TaskDataEntity

    @Delete(entity = TaskDataEntity::class)
    suspend fun deleteTask(taskDataEntity: TaskDataEntity)
}