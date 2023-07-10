package com.example.mydiary.data.datasource.local.mock

import com.google.gson.annotations.SerializedName

data class TaskMockEntity (
    @SerializedName("id")
    val id: Int,
    @SerializedName("date_start")
    val dateStart: Long,
    @SerializedName("date_finish")
    val dateFinish: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String
)