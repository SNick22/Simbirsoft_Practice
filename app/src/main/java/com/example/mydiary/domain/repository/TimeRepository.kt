package com.example.mydiary.domain.repository

import com.example.mydiary.domain.entity.TimeEntity

interface TimeRepository {

    fun getCurrentTime(): TimeEntity
}