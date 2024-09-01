package com.razorquake.sih2k24.text_translation

import androidx.annotation.DrawableRes
import com.razorquake.sih2k24.R

data class TTState(
    val query: String = "",
    val isRecording: Boolean = false,
    val error: String? = null,
    val rmsValues: List<Float> = emptyList(),
    @DrawableRes val image: Int = R.drawable.space
)