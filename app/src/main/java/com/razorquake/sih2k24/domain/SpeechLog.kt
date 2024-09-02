package com.razorquake.sih2k24.domain

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class SpeechLog(
    @PrimaryKey val text: String,
    val timestamp: LocalDateTime = LocalDateTime.now()
)
