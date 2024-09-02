package com.razorquake.sih2k24.domain.repository

import com.razorquake.sih2k24.domain.SpeechLog
import kotlinx.coroutines.flow.Flow

interface AppRepository {
    suspend fun insertSpeechLog(speechLog: SpeechLog)
    suspend fun deleteSpeechLog(speechLog: SpeechLog)
    fun getAllSpeechLogs(): Flow<List<SpeechLog>>
    suspend fun getSpeechLog(text: String): SpeechLog?
}