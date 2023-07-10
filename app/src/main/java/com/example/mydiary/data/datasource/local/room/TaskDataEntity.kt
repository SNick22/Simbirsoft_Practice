package com.example.mydiary.data.datasource.local.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mydiary.data.datasource.local.room.TaskDataEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class TaskDataEntity(

    @PrimaryKey(true)
    @ColumnInfo("id")
    val id: Int = 0,

    @ColumnInfo("date_start")
    val dateStart: Long,

    @ColumnInfo("date_finish")
    val dateFinish: Long,

    @ColumnInfo("name")
    val name: String,

    @ColumnInfo("description")
    val description: String
) {
    companion object {
        const val TABLE_NAME = "task"
    }
}
