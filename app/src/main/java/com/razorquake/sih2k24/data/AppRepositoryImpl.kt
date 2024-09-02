package com.razorquake.sih2k24.data

import com.razorquake.sih2k24.data.local.SpeechLogDao
import com.razorquake.sih2k24.domain.SpeechLog
import com.razorquake.sih2k24.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow

class AppRepositoryImpl(
    private val speechLogDao: SpeechLogDao
): AppRepository {
    override suspend fun insertSpeechLog(speechLog: SpeechLog) {
        speechLogDao.insertSpeechLog(speechLog)
    }

    override suspend fun deleteSpeechLog(speechLog: SpeechLog) {
        speechLogDao.deleteSpeechLog(speechLog)
    }

    override fun getAllSpeechLogs(): Flow<List<SpeechLog>> {
        return speechLogDao.getAllSpeechLogs()
    }

    override suspend fun getSpeechLog(text: String): SpeechLog? {
        return speechLogDao.getSpeechLog(text)
    }
}