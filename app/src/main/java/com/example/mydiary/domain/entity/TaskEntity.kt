package com.example.mydiary.domain.entity

data class TaskEntity (
    val id: Int = 0,
    val dateStart: Long,
    val dateFinish: Long,
    val name: String,
    val description: String
)
