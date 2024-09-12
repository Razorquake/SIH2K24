package com.razorquake.sih2k24.presentation.history

import com.razorquake.sih2k24.domain.models.SpeechLog

data class HistoryState(
    val speechLogs: List<SpeechLog> = emptyList(),
)