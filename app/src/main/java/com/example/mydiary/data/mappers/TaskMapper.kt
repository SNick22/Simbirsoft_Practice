package com.example.mydiary.data.mappers

import com.example.mydiary.data.datasource.local.mock.TaskMockEntity
import com.example.mydiary.data.datasource.local.room.TaskDataEntity
import com.example.mydiary.domain.entity.TaskEntity

fun TaskDataEntity.toTaskEntity(): TaskEntity =
    TaskEntity(
        id = id,
        dateStart = dateStart,
        dateFinish = dateFinish,
        name = name,
        description = description
    )

fun TaskEntity.toTaskDataEntity(): TaskDataEntity =
    TaskDataEntity(
        id = id,
        dateStart = dateStart,
        dateFinish = dateFinish,
        name = name,
        description = description
    )

fun TaskMockEntity.toTaskDataEntity(): TaskDataEntity =
    TaskDataEntity(
        id = id,
        dateStart = dateStart,
        dateFinish = dateFinish,
        name = name,
        description = description
    )
