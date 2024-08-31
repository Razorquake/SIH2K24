package com.razorquake.sih2k24.text_translation

data class TTState(
    val query: String = "",
    val isRecording: Boolean = false,
    val error: String? = null,
    val rmsValues: List<Float> = emptyList()
)