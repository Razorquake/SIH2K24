package com.razorquake.sih2k24.domain.usecases

import com.razorquake.sih2k24.domain.models.SpeechLog
import com.razorquake.sih2k24.domain.repository.AppRepository

class DeleteSpeechLog(
    private val repository: AppRepository
) {
    suspend operator fun invoke(speechLog: SpeechLog){
        repository.deleteSpeechLog(speechLog)
    }
}