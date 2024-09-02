package com.razorquake.sih2k24.domain.usecases

data class AppUseCases(
    val deleteSpeechLog: DeleteSpeechLog,
    val insertSpeechLog: InsertSpeechLog,
    val getSpeechLog: GetSpeechLog,
    val getAllSpeechLog: GetAllSpeechLog
)
