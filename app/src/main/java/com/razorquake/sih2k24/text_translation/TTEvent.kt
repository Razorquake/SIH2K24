package com.razorquake.sih2k24.text_translation

import android.content.Context

sealed class TTEvent {
    data class UpdateQuery(val query: String) : TTEvent()
    data class UpdateIsRecording(val isRecording: Boolean) : TTEvent()
    data class StartRecording(val context: Context, val onResult: (String) -> Unit) : TTEvent()
    data object StopRecording : TTEvent()
    data class RecordingError(val error: String) : TTEvent()
}