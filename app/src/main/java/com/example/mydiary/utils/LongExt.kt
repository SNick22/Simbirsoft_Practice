package com.example.mydiary.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun Long.getTime(): String {
    val dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(this), ZoneId.systemDefault())
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return dateTime.format(formatter)
}