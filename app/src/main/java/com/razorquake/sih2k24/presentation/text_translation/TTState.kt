package com.razorquake.sih2k24.presentation.text_translation

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.razorquake.sih2k24.R

data class TTState(
    val query: String = "",
    val isRecording: Boolean = false,
    val error: String? = null,
    val rmsValues: List<Float> = emptyList(),
    @DrawableRes val image: Int = R.drawable.space,
    @ColorRes val textColors: List<Int> = emptyList()
) {
    fun updateTextColors(): TTState {
        return this.copy(textColors = List(query.length) { R.color.body })
    }
}