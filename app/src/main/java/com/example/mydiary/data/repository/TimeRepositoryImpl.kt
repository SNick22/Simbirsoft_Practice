package com.example.mydiary.data.repository

import com.example.mydiary.data.mappers.toTimeEntity
import com.example.mydiary.domain.entity.TimeEntity
import com.example.mydiary.domain.repository.TimeRepository
import java.time.LocalTime
import javax.inject.Inject

class TimeRepositoryImpl @Inject constructor(): TimeRepository {

    override fun getCurrentTime(): TimeEntity = LocalTime.now().toTimeEntity()
}