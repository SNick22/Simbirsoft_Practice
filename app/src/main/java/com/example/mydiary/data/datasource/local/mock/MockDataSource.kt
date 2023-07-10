package com.example.mydiary.data.datasource.local.mock

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

class MockDataSource @Inject constructor(
    private val context: Context
) {

    fun getMock(): List<TaskMockEntity> {
        val gson = Gson()
        val json = context.assets
            .open("mock.json")
            .bufferedReader().use {
                it.readText()
            }
        val itemType = object : TypeToken<List<TaskMockEntity>>() {}.type
        return gson.fromJson(json, itemType)
    }
}