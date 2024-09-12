package com.razorquake.sih2k24.domain.usecases

import com.razorquake.sih2k24.domain.models.SpeechLog
import com.razorquake.sih2k24.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow

class GetAllSpeechLog(
    private val repository: AppRepository
) {
    operator fun invoke(): Flow<List<SpeechLog>> = repository.getAllSpeechLogs()
}