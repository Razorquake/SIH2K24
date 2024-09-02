package com.razorquake.sih2k24.domain.usecases

import com.razorquake.sih2k24.domain.SpeechLog
import com.razorquake.sih2k24.domain.repository.AppRepository

class InsertSpeechLog(
    private val repository: AppRepository
) {
    suspend operator fun invoke(speechLog: SpeechLog){
        repository.insertSpeechLog(speechLog)
    }
}