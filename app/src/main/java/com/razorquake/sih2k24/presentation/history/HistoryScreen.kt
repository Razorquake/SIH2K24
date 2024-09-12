package com.razorquake.sih2k24.presentation.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.razorquake.sih2k24.domain.models.SpeechLog
import com.razorquake.sih2k24.presentation.history.components.SpeechLog

@Composable
fun HistoryScreen(
    modifier: Modifier,
    state: HistoryState,
    onEvent: (HistoryEvent) -> Unit,
    navigateToDetails: (SpeechLog)->Unit
) {
    Column(modifier.fillMaxSize()) {
        Text("Speech Logs", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(16.dp))
        state.speechLogs.let {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(it.size) { index ->
                    val speechLog = it[index]
                    SpeechLog(
                        modifier = Modifier.fillMaxWidth(),
                        speechLog = speechLog,
                        onClick = {navigateToDetails(it[index])},
                        onDelete = { onEvent(HistoryEvent.DeleteSpeechLog(speechLog.text)) }
                    )

                }
            }
        }
    }
}