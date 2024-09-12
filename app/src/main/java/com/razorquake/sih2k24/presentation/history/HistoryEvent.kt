package com.razorquake.sih2k24.presentation.history

sealed class HistoryEvent {
    data class DeleteSpeechLog(val text: String): HistoryEvent()
}