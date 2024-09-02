package com.razorquake.sih2k24.presentation.history

import com.razorquake.sih2k24.domain.SpeechLog

sealed class HistoryEvent {
    data class DeleteSpeechLog(val text: String): HistoryEvent()
}