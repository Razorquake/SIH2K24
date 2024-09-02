package com.razorquake.sih2k24.domain.usecases

import com.razorquake.sih2k24.domain.repository.AppRepository

class GetSpeechLog(
    private val repository: AppRepository
) {
    suspend operator fun invoke(text: String) = repository.getSpeechLog(text)
}