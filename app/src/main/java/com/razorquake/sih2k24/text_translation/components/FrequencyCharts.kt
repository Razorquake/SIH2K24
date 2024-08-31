package com.razorquake.sih2k24.text_translation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FrequencyChart(rmsValues: List<Float>) {
    val barCount = 40
    val barWidth = 4.dp

    val recentRmsValues = rmsValues.takeLast(barCount)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        for (rmsValue in recentRmsValues) {
            val heightFactor = rmsValue / 10f
            Box(
                modifier = Modifier
                    .width(barWidth)
                    .fillMaxHeight(fraction = heightFactor)
                    .background(MaterialTheme.colorScheme.primary)
                    .align(Alignment.Bottom),
            )
        }
    }
}